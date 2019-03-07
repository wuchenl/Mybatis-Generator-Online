package com.letters7.wuchen.demo.generator.support;

import com.letters7.wuchen.demo.generator.model.DBType;
import com.letters7.wuchen.demo.generator.model.DatabaseConfig;
import com.letters7.wuchen.demo.generator.model.GeneratorConfig;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.TableConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuchen   2019/1/18 13:49
 * @version 0.1
 * Mybatis-Generator 操作核心。同时处理插件
 */
public class MybatisGeneratorBuilder {
    /**
     * 日志提供器
     */
    private static final Logger log = LoggerFactory.getLogger(MybatisGeneratorBuilder.class);

    public MybatisGeneratorBuilder() {

    }

    public boolean generator(GeneratorConfig generatorConfig, DatabaseConfig dataBaseConfig) {
        // 使用的数据源类型
        String dbType = dataBaseConfig.getDbType();
        // Mybatis配置
        Configuration configuration = new Configuration();
        Context context = new Context(ModelType.CONDITIONAL);
        configuration.addContext(context);
        // 自动识别关键字
        context.addProperty("autoDelimitKeywords", "true");
        // beginningDelimiter和endingDelimiter：指明数据库的用于标记数据库对象名的符号，比如ORACLE就是双引号，MYSQL默认是`反引号
        context.addProperty("beginningDelimiter", "`");
        context.addProperty("endingDelimiter", "`");

        // 生成文件的编码
        context.addProperty("javaFileEncoding", "UTF-8");

        // table相关配置
        TableConfiguration tableConfig = new TableConfiguration(context);
        tableConfig.setTableName(generatorConfig.getTableName());
        tableConfig.setDomainObjectName(generatorConfig.getDomainObjectName());

        // 针对 postgresql 数据库保留字需要单独配置
        if (DBType.PostgreSQL.name().equals(dbType)) {
            tableConfig.setDelimitIdentifiers(true);
        }
        // 不生成实例数据时
        if (!generatorConfig.isUseExample()) {
            tableConfig.setUpdateByExampleStatementEnabled(false);
            tableConfig.setCountByExampleStatementEnabled(false);
            tableConfig.setDeleteByExampleStatementEnabled(false);
            tableConfig.setSelectByExampleStatementEnabled(false);
        }

        // 如果是Mysql相关使用schema，别的使用catalog
        if (DBType.MySQL5.name().equals(dbType) || DBType.MySQL8.name().equals(dbType)) {
            tableConfig.setSchema(dataBaseConfig.getSchema());
        } else {
            tableConfig.setCatalog(dataBaseConfig.getSchema());
        }
        // 是否使用Schema前缀
        handUseSchemaPrefix(generatorConfig, dataBaseConfig, tableConfig, dbType);
        return false;
    }

    /**
     * @param generatorConfig
     * @param dataBaseConfig
     * @param tableConfig
     * @param dbType
     */
    private void handUseSchemaPrefix(GeneratorConfig generatorConfig, DatabaseConfig dataBaseConfig, TableConfiguration tableConfig, String dbType) {
        if (generatorConfig.isUseSchemaPrefix()) {
            if (DBType.MySQL5.name().equals(dbType) || DBType.MySQL8.name().equals(dbType)) {
                tableConfig.setSchema(dataBaseConfig.getSchema());
            } else if (DBType.Oracle.name().equals(dbType)) {
                //Oracle的schema为用户名，如果连接用户拥有dba等高级权限，若不设schema，会导致把其他用户下同名的表也生成一遍导致mapper中代码重复
                tableConfig.setSchema(dataBaseConfig.getUsername());
            } else {
                // 别的就使用catalog
                tableConfig.setCatalog(dataBaseConfig.getSchema());
            }
        }
    }
}
