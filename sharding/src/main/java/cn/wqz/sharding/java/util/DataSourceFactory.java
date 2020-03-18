package cn.wqz.sharding.java.util;

import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 数据源获取工厂
 */
public class DataSourceFactory {

    /**
     * 配置数据源映射
     */
    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        // 创建两个数据源，分别对应两个数据库ds_0,ds_1数据库
        result.put("ds_0", DataSourceUtils.createDataSource("ds_0"));
        result.put("ds_1", DataSourceUtils.createDataSource("ds_1"));
        return result;
    }

    /**
     * 获取数据源对象
     * @return
     * @throws SQLException
     */
    public static DataSource getDataSource() throws SQLException {
        // 配置数据源映射
        Map<String, DataSource> dataSourceMap = createDataSourceMap();

        // 配置表规则
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration("t_order");

        // 配置主键生成
        tableRuleConfiguration.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "order_id"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        // 一对多的关系，一个分片规则，对应多个表规则
        shardingRuleConfiguration.getTableRuleConfigs().add(tableRuleConfiguration);

        // 配置默认分库策略
        shardingRuleConfiguration.setDefaultDatabaseShardingStrategyConfig(
                new InlineShardingStrategyConfiguration("user_id", "ds_${user_id % 2}")
        );
        // 获取数据源对象
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration, new Properties());
    }
}