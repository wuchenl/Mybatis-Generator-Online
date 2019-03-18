package com.letters7.wuchen.demo.generator.controller;

import com.google.common.collect.Lists;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.GeneratorConfig;
import com.letters7.wuchen.demo.generator.model.ModelFiledBO;
import com.letters7.wuchen.demo.generator.service.DataSourceService;
import com.letters7.wuchen.demo.generator.support.UtilDataBase;
import com.letters7.wuchen.sdk.ResponseMessage;
import com.letters7.wuchen.springboot2.utils.collection.UtilCollection;
import com.letters7.wuchen.springboot2.utils.string.UtilString;
import com.letters7.wuchen.springboot2.utils.web.UtilWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/26 17:15
 * @desc 基于数据源的相关操作Controller
 */
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    /**
     * 日志提供器
     */
    private final static Logger log = LoggerFactory.getLogger(DataSourceController.class);

    @Autowired
    private DataSourceService dataSourceService;

    /**
     * 获取当前保存好的数据源
     *
     * @return
     */
    @GetMapping("/host")
    public ResponseMessage getHostDataSource() {
        String currentIpBySpring = UtilWeb.getCurrentIpBySpring();
        List<DatabaseConfig> dataSource = dataSourceService.getDataSourceListByHost(currentIpBySpring);
        if (Objects.nonNull(dataSource)) {
            return ResponseMessage.ok(dataSource);
        }
        return ResponseMessage.error("");
    }

    /**
     * 保存填写好的数据源
     *
     * @param databaseConfig 数据源配置
     * @return 是否保存成功
     */
    @PostMapping("/host")
    public ResponseMessage addHostDataSource(@RequestBody DatabaseConfig databaseConfig) {
        if (Objects.nonNull(databaseConfig)) {
            Connection connection = UtilDataBase.getConnection(databaseConfig);
            if (Objects.isNull(connection)) {
                return ResponseMessage.error("该数据源无法连接上！");
            }
            String currentIpBySpring = UtilWeb.getCurrentIpBySpring();
            List<DatabaseConfig> databaseConfigList = dataSourceService.getDataSourceListByHost(currentIpBySpring);
            if (UtilCollection.isEmpty(databaseConfigList)) {
                databaseConfigList = Lists.newArrayList();
            }
            databaseConfigList.add(databaseConfig);
            dataSourceService.saveDataSource(currentIpBySpring, databaseConfigList);
            return ResponseMessage.ok();
        }
        return ResponseMessage.error("");
    }

    /**
     * 测试填写好的数据源
     *
     * @param databaseConfig 数据源配置
     * @return 是否保存成功
     */
    @PostMapping("/test")
    public ResponseMessage testConnection(@RequestBody DatabaseConfig databaseConfig) {
        // 非空时，进行连接。如果没问题则返回成功
        if (Objects.nonNull(databaseConfig)) {
            Connection connection = UtilDataBase.getConnection(databaseConfig);
            if (Objects.nonNull(connection)) {
                return ResponseMessage.ok("");
            }
        }
        return ResponseMessage.error("");
    }


    /**
     * 获取本数据源的连接信息
     *
     * @return 表信息
     */
    @GetMapping("/tables")
    public ResponseMessage getHostTables() {
        String host = UtilWeb.getCurrentIpBySpring();
        if (UtilString.isEmpty(host)) {
            return ResponseMessage.error("获取当前IP信息异常！请稍后再试！");
        }
        List<String> tables = dataSourceService.getHostTables(host, "");
        return ResponseMessage.ok(tables);
    }

    /**
     * 根据表名获取对应的字段信息
     *
     * @param tableName 表名
     * @return 字段信息
     */
    @GetMapping("/table/{tableName}")
    public ResponseMessage getTableByName(@PathVariable("tableName") String tableName) {
        String host = UtilWeb.getCurrentIpBySpring();
        if (UtilString.isEmpty(host)) {
            return ResponseMessage.error("获取当前IP信息异常！请稍后再试！");
        }
        List<ModelFiledBO> tableColumns = dataSourceService.getTableColumnsByName(host, "", tableName);
        return ResponseMessage.ok(tableColumns);
    }

    /**
     * 根据表名生成对应实体类名以及Dao名
     *
     * @param tableName 表名
     * @return 对应名称
     */
    @GetMapping("/name/{tableName}")
    public ResponseMessage handTableName(@PathVariable String tableName) {
        if (UtilString.isNotEmpty(tableName)) {
            GeneratorConfig generatorConfig = GeneratorConfig.builder().build();
            generatorConfig.setDomainObjectName(UtilString.toCapitalizeCamelCase(tableName))
                    .setMapperName(UtilString.join(generatorConfig.getDomainObjectName(), "Dao"));
            return ResponseMessage.ok(generatorConfig);
        }
        return ResponseMessage.ok();
    }
}
