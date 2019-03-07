package com.letters7.wuchen.demo.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author wuchen on 2019/1/14 15:10
 * @version 0.1
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceTreeBO implements Serializable {
    /**
     * 数据源名称
     */
    private String dataSourceName;
    /**
     * 数据源下表的名称
     */
    private List<String> tableNames;
}
