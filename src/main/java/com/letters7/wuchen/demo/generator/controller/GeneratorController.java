package com.letters7.wuchen.demo.generator.controller;

import com.google.common.collect.Lists;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.GeneratorConfig;
import com.letters7.wuchen.demo.generator.service.DataSourceService;
import com.letters7.wuchen.demo.generator.support.GeneratorConst;
import com.letters7.wuchen.demo.generator.support.MybatisGeneratorBridge;
import com.letters7.wuchen.demo.generator.support.UtilCache;
import com.letters7.wuchen.sdk.ResponseMessage;
import com.letters7.wuchen.sdk.exception.BusinessException;
import com.letters7.wuchen.springboot2.utils.exception.UtilException;
import com.letters7.wuchen.springboot2.utils.string.UtilString;
import com.letters7.wuchen.springboot2.utils.web.UtilWeb;
import com.letters7.wuchen.springboot2.utils.zip.UtilZip;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.IgnoredColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wuchen
 * @version 0.1 2018/12/28 10:13
 * 用于处理生成
 */
@RestController
@RequestMapping("/generator")
public class GeneratorController {
    /**
     * 日志提供器
     */
    private final static Logger log = LoggerFactory.getLogger(GeneratorController.class);

    @Autowired
    private DataSourceService dataSourceService;


    /**
     * 生成相关配置文件，并弄成压缩包
     *
     * @param generatorConfig 配置信息
     * @return 压缩包
     */
    @PostMapping("/single")
    public ResponseMessage generator(@RequestBody GeneratorConfig generatorConfig) {

        // 获取数据源
        String host = UtilWeb.getCurrentIpBySpring();
        DatabaseConfig dataSource = dataSourceService.getDataSourceBySourceName(host, generatorConfig.getDatasourceName());
        if (Objects.isNull(dataSource)) {
            return ResponseMessage.error("获取当前数据源配置信息异常，请重新配置后重试！");
        }
        File file = new File(UtilString.join(host, dataSource.getName()));
        if (!file.exists()) {
            file.mkdir();
            log.info("路径为:{}", file.getAbsolutePath());
        }
        // 设置默认值
        generatorConfig.setProjectFolder(file.getAbsolutePath());
        if (!checkDirs(generatorConfig)) {
            log.info("创建文件夹！");
        }

        // 忽略的行数
        List<IgnoredColumn> ignoredColumns = Lists.newArrayList();
        // 重写的行数
        List<ColumnOverride> columnOverrides = Lists.newArrayList();

        MybatisGeneratorBridge bridge = new MybatisGeneratorBridge();
        bridge.setGeneratorConfig(generatorConfig);
        bridge.setDatabaseConfig(dataSource);
        bridge.setIgnoredColumns(ignoredColumns);
        bridge.setColumnOverrides(columnOverrides);
        try {
            bridge.generate();
            String zipPath = UtilString.join(generatorConfig.getProjectFolder(), ".zip");
            UtilZip.pack(generatorConfig.getProjectFolder(), zipPath);
            // 生成ZIP后，将路径存入缓存中。并将key返回给页面
            String cacheName = UtilString.join(generatorConfig.getDatasourceName(), "", generatorConfig.getTableName(), ".zip");
            UtilCache.putDataToCache(GeneratorConst.CACHE_ZIPS, cacheName, zipPath);
            log.info("ZIP文件{}生成完毕并放入缓存中:{}", zipPath, cacheName);
            return ResponseMessage.ok(cacheName);
        } catch (Exception e) {
            log.error("生成配置文件出现异常:{}", UtilException.getBootMessage(e));
            throw new BusinessException(UtilException.getBootMessage(e));
        }
    }

    /**
     * 下载压缩好的ZIP
     *
     * @param cacheKey 缓存的key
     * @return 对应ZIP包的流
     */
    @GetMapping("download/{cacheKey:.+}")
    public ResponseMessage downloadZip(@PathVariable @NotEmpty String cacheKey, HttpServletResponse response) {
        log.info("即将开始下载ZIP文件:{}", cacheKey);
        String zipPath = UtilCache.getDataFromCache(GeneratorConst.CACHE_ZIPS, cacheKey);
        if (UtilString.isEmpty(zipPath)) {
            log.error("缓存中已经没有对应ZIP缓存信息!");
            return ResponseMessage.error("缓存中已经没有对应ZIP缓存信息！");
        }
        File file = new File(zipPath);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            String errorMsg = UtilException.getBootMessage(e);
            log.error("读取下载文件出现异常：{}", errorMsg);
            return ResponseMessage.error(errorMsg);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    String errorMsg = UtilException.getBootMessage(e);
                    log.error("关闭流：{}", errorMsg);
                }
            }
        }

        return ResponseMessage.ok();
    }


    /**
     * 检查并创建不存在的文件夹
     *
     * @return 操作是否成功
     */
    private boolean checkDirs(GeneratorConfig config) {
        List<String> dirs = new ArrayList<>();
        dirs.add(config.getProjectFolder());
        dirs.add(FilenameUtils.normalize(config.getProjectFolder().concat("/").concat(config.getModelPackageTargetFolder())));
        dirs.add(FilenameUtils.normalize(config.getProjectFolder().concat("/").concat(config.getDaoTargetFolder())));
        dirs.add(FilenameUtils.normalize(config.getProjectFolder().concat("/").concat(config.getMappingXMLTargetFolder())));
        boolean haveNotExistFolder = false;
        for (String dir : dirs) {
            File file = new File(dir);
            if (!file.exists()) {
                haveNotExistFolder = true;
            }
        }
        if (haveNotExistFolder) {
            try {
                for (String dir : dirs) {
                    FileUtils.forceMkdir(new File(dir));
                }
                return true;
            } catch (Exception e) {
                log.warn("创建目录失败，请检查目录是否是文件而非目录");
                return false;
            }
        }
        return true;
    }
}
