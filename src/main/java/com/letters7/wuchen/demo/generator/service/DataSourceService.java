package com.letters7.wuchen.demo.generator.service;

import com.letters7.wuchen.demo.generator.model.DataSourceTreeBO;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.ModelFiledBO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/27 16:35
 * @desc
 */
public interface DataSourceService {
    /**
     * 保存数据库配置信息
     *
     * @param host           访问ip
     * @param databaseConfig 数据库配置信息
     */
    void saveDataSource(String host, List<DatabaseConfig> databaseConfig);


    /**
     * 获取当前IP所对应的数据源的所有表
     *
     * @param host IP信息
     * @return 对应的表名
     */
    List<String> getHostTables(String host,String sourceName);

    /**
     * 根据IP和表名获取对应表字段信息
     *
     * @param host      IP信息
     * @param tableName 对应的表名
     * @return 字段信息
     */
    List<ModelFiledBO> getTableColumnsByName(String host,String sourceName, String tableName);

    /**
     * 获取当前IP的数据源配置信息
     * @param host IP
     * @return 对应数据源信息
     */
    List<DatabaseConfig> getDataSourceListByHost(String host);

    /**
     * 获取当前IP的某个数据源配置信息
     * @param host IP
     * @param sourceName 数据源名称
     * @return 对应配置信息
     */
    DatabaseConfig getDataSourceBySourceName(String host,String sourceName);


    /**
     * 获取某个数据源下表列表
     * @param host
     * @return
     */
    List<DataSourceTreeBO> getDataSourceTree(String host);
}
