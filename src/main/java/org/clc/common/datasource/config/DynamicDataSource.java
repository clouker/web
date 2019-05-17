package org.clc.common.datasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {


    public final static String dataSourceDefault = "dataSourceDefault";
    public final static String dataSourceTest = "dataSourceTest";

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicContextHolder.peek();
    }

}
