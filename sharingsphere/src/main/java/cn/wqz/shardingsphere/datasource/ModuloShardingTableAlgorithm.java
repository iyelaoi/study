package cn.wqz.shardingsphere.datasource;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

public final class ModuloShardingTableAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        for(String each : collection){
            if(each.endsWith(preciseShardingValue.getValue() % 2 + "")){
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }
}
