package com.chengh.db.util.algorithm;

import java.util.Collection;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;



/**
 * PreciseShardingAlgorithm是必选的，用于处理=和IN的分片
 * @program: ShardingJdbcDemo
 * @description:
 * @author: chengh
 * @create: 2019-07-01 00:41
 *
 */
public class IdSharingAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     *
     * @param collection actual表名集合
     * @param preciseShardingValue 封装了列名与值，用于分表
     *                   table-strategy.standard.sharding-column: id
     * @return 返回actual表名
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        Long id = preciseShardingValue.getValue(); // 获取值，已经配置了id为分表依赖的列
        for (String name : collection) { // 遍历表名
            if (name.endsWith(id % collection.size() + "")) { // 按余数返回表名
                return name;
            }
        }
        throw new IllegalArgumentException();
    }
}
