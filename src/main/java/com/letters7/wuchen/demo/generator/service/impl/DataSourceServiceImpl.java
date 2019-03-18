package com.letters7.wuchen.demo.generator.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.letters7.wuchen.demo.generator.model.DataSourceTreeBO;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.ModelFiledBO;
import com.letters7.wuchen.demo.generator.service.DataSourceService;
import com.letters7.wuchen.demo.generator.support.GeneratorConst;
import com.letters7.wuchen.demo.generator.support.UtilCache;
import com.letters7.wuchen.demo.generator.support.UtilDataBase;
import com.letters7.wuchen.sdk.exception.BusinessException;
import com.letters7.wuchen.springboot2.utils.collection.UtilCollection;
import com.letters7.wuchen.springboot2.utils.exception.ExceptionHelper;
import com.letters7.wuchen.springboot2.utils.string.UtilString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * @author wuchen
 * @version 0.1
 * 2018/12/27 16:36
 * 用于保存和获取数据库连接配置信息
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    /**
     * 日志提供器
     */
    private static final Logger log = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    /**
     * 保存数据库配置信息
     *
     * @param host           请求者ip信息
     * @param databaseConfig 数据库配置信息
     */
    @Override
    public void saveDataSource(String host, List<DatabaseConfig> databaseConfig) {
        log.info("即将保存的数据库配置信息的key为:{}，配置信息为:{}", host, databaseConfig);
        UtilCache.putDataToCache(GeneratorConst.CACHE_DATASOURCE, host, JSON.toJSONString(databaseConfig));
    }


    /**
     * 获取当前IP所对应的数据源的所有表
     *
     * @param host       IP信息
     * @param sourceName 数据源配置信息
     * @return 对应的表名
     */
    @Override
    public List<String> getHostTables(String host, String sourceName) {
        List<String> tableNames = Lists.newArrayList();
        if (UtilString.isNotEmpty(host)) {
            DatabaseConfig databaseConfig = getDataSourceBySourceName(host, sourceName);
            if (Objects.nonNull(databaseConfig)) {
                try {
                    tableNames = UtilDataBase.getTableNames(databaseConfig);
                } catch (SQLException e) {
                    log.error("获取所有表信息出现异常：{}", ExceptionHelper.getBootMessage(e));
                    throw new BusinessException("获取所有表信息出现异常：" + ExceptionHelper.getBootMessage(e));
                }
            }
        }

        return tableNames;
    }

    /**
     * 根据IP和表名获取对应表字段信息
     *
     * @param host       IP信息
     * @param sourceName 数据源配置名称
     * @param tableName  对应的表名
     * @return 字段信息
     */
    @Override
    public List<ModelFiledBO> getTableColumnsByName(String host, String sourceName, String tableName) {
        List<ModelFiledBO> modelFiledBOS;
        DatabaseConfig databaseConfig = getDataSourceBySourceName(host, sourceName);
        try {
            modelFiledBOS = UtilDataBase.getTableColumns(databaseConfig, tableName);
        } catch (SQLException e) {
            log.error("获取表字段信息出现异常：{}", ExceptionHelper.getBootMessage(e));
            throw new BusinessException("获取表字段信息出现异常：" + ExceptionHelper.getBootMessage(e));
        }
        return modelFiledBOS;
    }

    /**
     * 获取当前IP的数据源配置信息
     *
     * @param host IP
     * @return 对应数据源信息
     */
    @Override
    public List<DatabaseConfig> getDataSourceListByHost(String host) {
        String result = UtilCache.getDataFromCache(GeneratorConst.CACHE_DATASOURCE, host);
        if (UtilString.isNotEmpty(result)) {
            return JSONArray.parseArray(result, DatabaseConfig.class);
        }
        return Lists.newArrayList();
    }

    /**
     * 获取当前IP的某个数据源配置信息
     *
     * @param host       IP
     * @param sourceName 数据源名称
     * @return 对应配置信息
     */
    @Override
    public DatabaseConfig getDataSourceBySourceName(String host, String sourceName) {
        DatabaseConfig databaseConfig = null;
        // 获取多条，在根据流去查找匹配
        List<DatabaseConfig> databaseConfigList = getDataSourceListByHost(host);
        if (UtilCollection.isNotEmpty(databaseConfigList)) {
            databaseConfig = databaseConfigList.stream().filter(dataConfig -> dataConfig.getName().equals(sourceName)).findAny().orElse(null);
        }
        return databaseConfig;
    }

    /**
     * 获取某个数据源下表列表
     *
     * @param host 数据源
     * @return 列表
     */
    @Override
    public List<DataSourceTreeBO> getDataSourceTree(String host) {
        List<DataSourceTreeBO> dataSourceTreeBOList = Lists.newArrayList();
        // 遍历当前有多少数据源配置信息
        List<DatabaseConfig> dataSourceList = getDataSourceListByHost(host);
        if (UtilCollection.isNotEmpty(dataSourceList)) {
            // 遍历数据源获取相关的表信息
            for (DatabaseConfig databaseConfig : dataSourceList) {
                DataSourceTreeBO dataSourceTreeBO = DataSourceTreeBO.builder().build();
                List<String> hostTables = getHostTables(host, databaseConfig.getName());
                dataSourceTreeBO.setDataSourceName(databaseConfig.getName()).setTableNames(hostTables);
                dataSourceTreeBOList.add(dataSourceTreeBO);
            }
        }
        return dataSourceTreeBOList;
    }
}
