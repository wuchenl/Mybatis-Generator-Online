package com.letters7.wuchen.demo.generator.support;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/27 14:47
 * @desc
 */
public class GeneratorConst {
    /**
     * 数据连接时的用户名
     */
    public static final String DATABASE_USER = "user";
    /**
     * 数据库连接时的密码
     */
    public static final String DATABASE_PASSWORD = "password";
    /**
     * utf8编码
     */
    public static final String ENCODE_UTF8 = "utf-8";
    /**
     * JDBC获取所有表时的参数
     */
    public static final String DATABASE_TABLE = "TABLE";
    /**
     * JDBC获取视图时的参数
     */
    public static final String DATABASE_VIEW = "VIEW";
    /**
     * mysql和postgresql 查询全表时的参数
     */
    public static final String DATABASE_ALL = "%";
    /**
     * 获取表的字段名的参数
     */
    public static final String DATABASE_COLUMN_NAME = "COLUMN_NAME";
    /**
     * 获取表的字段类型的参数
     */
    public static final String DATABASE_TYPE_NAME = "TYPE_NAME";
    /**
     * 缓存中的cache名称
     */
    public static final String CACHE_DATASOURCE = "dataSources";
    /**
     * 缓存中的压缩包路径所用的名称
     */
    public static final String CACHE_ZIPS = "dataZips";
    /**
     * 页面-静态变量-数据库驱动类型
     */
    public static final String URL_DB_TYPE = "dbType";

    /**
     * 页面-静态变量-是否已经有数据源配置
     */
    public static final String URL_HAS_DATASOURCE = "hasDataSource";

    /**
     * 页面-静态变量-数据源配置
     */
    public static final String URL_DATASOURCE_INFO = "dataSource";

}
