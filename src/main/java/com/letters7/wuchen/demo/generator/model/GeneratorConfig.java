package com.letters7.wuchen.demo.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.IgnoredColumn;

import java.util.List;

/**
 * @author wlone
 * @date 2018-12-27 13:47:34
 * @desc 生成对应实体时的相关配置
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GeneratorConfig {

    /**
     * 自定义列名
     */
    List<ModelFiledBO> modelFiledList;
    /**
     * 本配置的名称
     */
    private String name;
    /**
     * 数据库连接驱动jar包位置
     */
    private String connectorJarPath;
    /**
     * 项目路径---或者说到时存放的路径
     */
    private String projectFolder;
    /**
     * Model 类存在的包名
     */
    private String modelPackage;
    /**
     * model类存在的路径----在项目路径里面追加，默认为：src/main/java
     */
    private String modelPackageTargetFolder;
    /**
     * Dao类的包名
     */
    private String daoPackage;
    /**
     * Dao 类存放的路径----项目路径后追加，默认为：src/main/java
     */
    private String daoTargetFolder;
    /**
     * Mapper 名称，或者说Dao 名称
     */
    private String mapperName;
    /**
     * Mapper 存放的包路径
     */
    private String mappingXMLPackage;
    /**
     * mapper文件存放的文件夹---基于项目路径，默认为：src/main/resource
     */
    private String mappingXMLTargetFolder;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 实体名或者说Model 类名
     */
    private String domainObjectName;
    /**
     * 查询限制
     */
    private boolean offsetLimit = false;
    /**
     * 生成实体类注释（根据表注释）
     */
    private boolean comment = false;
    /**
     * 是否覆盖原有xml。这里基于在线环境使用，默认为true
     */
    private boolean overrideXML = true;
    /**
     * 是否需要为model生成对应的 toString，equals，hashCode方法
     */
    private boolean needToStringHashcodeEquals = false;
    /**
     * 在查询的时候是否需要ForUpdate 语句
     */
    private boolean needForUpdate = false;
    /**
     * Dao 层是否需要使用@Repository 注解
     */
    private boolean annotationDAO = false;
    /**
     * 是否生成JPA注解
     */
    private boolean annotation = false;
    /**
     * 是否使用实际的列名
     */
    private boolean useActualColumnNames = false;
    /**
     * 是否使用Example
     */
    private boolean useExample = false;
    /**
     * 表主键，选填
     */
    private String generateKeys;
    /**
     * 生成文件的编码格式，默认为UTF-8
     */
    private String encoding;
    /**
     * 是否使用as
     */
    private boolean useTableNameAlias = false;
    /**
     * Dao 方法是否继承基础Dao，来简化，进行扩展
     */
    private boolean useDAOExtendStyle = false;
    /**
     * 使用Schema 前缀
     */
    private boolean useSchemaPrefix = false;
    /**
     * 是否支持JSR310
     */
    private boolean jsr310Support = false;
    /**
     * 数据源名称
     */
    private String datasourceName;
    /**
     * 是否使用Lombok注解生成model
     */
    private Boolean useLombok = false;

    /**
     * 生成时要忽略的列表
     */
    private List<IgnoredColumn> ignoredColumns;

    /**
     * 要被覆盖的
     */
    private List<ColumnOverride> columnOverrides;

}
