package com.chengh.db.util.algorithm;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.Range;

import io.shardingsphere.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;

/**
 *
 * RangeShardingAlgorithm是可选的，用于处理BETWEEN AND分片，
 * 如果不配置RangeShardingAlgorithm，SQL中的BETWEEN AND将按照全库路由处理。
 * @program: ShardingJdbcDemo
 * @description:
 * @author: chengh
 * @create: 2019-07-01 00:41
 */
public class IdRangeSharingAlgorithm implements RangeShardingAlgorithm<Long> {

    /**
     * 范围分片算法，当sql中使用 between and 时，分片键的值落于一个范围之内
     * 这个范围内的id对应的数据可能存在于多个表中，故将这些含有目标数据的表名筛选出来
     * @param collection
     * @param rangeShardingValue
     * @return 返回表集合，集合中的表含有between中对应的目标数据
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        Collection<String> collect = new ArrayList<>();
        Range<Long> valueRange = rangeShardingValue.getValueRange(); // 取得range
        for (Long i = valueRange.lowerEndpoint(); i <= valueRange.upperEndpoint(); i++) {
            for (String each : collection) {
                if (each.endsWith(i % collection.size() + "")) {
                    collect.add(each);
                }
            }
        }
        return collect;
    }
}
