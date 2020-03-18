package cn.wqz.sharding.java.util;


import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

/**
 * 数据源创建工具类
 */
public class DataSourceUtils {

    /**
     * 创建数据源对象
     */
    public static DataSource createDataSource(final String dataSourceName) {
        // 使用 DruidDataSource
        DruidDataSource dataSource = new DruidDataSource();
        String jdbcUrl =
                String.format(
                        "jdbc:mysql://localhost:3306/%s?useSSL=false&useUnicode=true&characterEncoding=UTF-8",
                        dataSourceName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername("root");
        dataSource.setPassword("461299");
        // 数据源其它配置（略）
        return dataSource;
    }
}
