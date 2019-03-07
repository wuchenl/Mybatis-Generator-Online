package com.letters7.wuchen.demo.generator.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.letters7.wuchen.demo.generator.model.DBType;
import com.letters7.wuchen.demo.generator.model.DataSourceTreeBO;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.service.DataSourceService;
import com.letters7.wuchen.demo.generator.support.GeneratorConst;
import com.letters7.wuchen.springboot2.utils.collection.UtilCollection;
import com.letters7.wuchen.springboot2.utils.web.UtilWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author wuchenl
 * @date 2019/1/3.
 */
@Controller
public class UrlController {

    /**
     * 日志提供器
     */
    private static final Logger log = LoggerFactory.getLogger(UrlController.class);

    @Autowired
    private DataSourceService dataSourceService;


    /**
     * 首页
     *
     * @param model 页面相关参数
     * @return 请求的页面
     */
    @GetMapping("/")
    public String index(Model model) {
        // 加载数据库驱动到页面
        List<DBType> dbTypes = Lists.newArrayList(DBType.values());
        model.addAttribute(GeneratorConst.URL_DB_TYPE, dbTypes);
        // 是否已经配置了数据源
        String host = UtilWeb.getCurrentIpBySpring();
        List<DataSourceTreeBO> dataSourceTreeBOList = dataSourceService.getDataSourceTree(host);
        boolean hasDatasource = false;
        if (UtilCollection.isNotEmpty(dataSourceTreeBOList)) {
            // 如果存在数据源配置时，加载相关数据源种的表名信息到页面中
            model.addAttribute(GeneratorConst.URL_DATASOURCE_INFO, JSON.toJSONString(dataSourceTreeBOList));
            hasDatasource = true;
        }
        model.addAttribute(GeneratorConst.URL_HAS_DATASOURCE, hasDatasource);
        log.info("【{}】开始加载相关配置，是否已有配置:{}", host, hasDatasource);
        return "index";
    }
}
