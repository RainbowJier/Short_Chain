package com.example.dcloud_link.strategy;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Collections;

/**
 * @Description：分库算法，根据短链码的第⼀位，选择真实配置的库
 * @Author： RainbowJier
 * @Data： 2024/11/2 11:27
 */
public class CustomTablePreciseShardingAlgorithm implements StandardShardingAlgorithm<String> {

    /**
     * 自定义分库算法
     * @param collection 数据源集合，例如：["shot_link_0", "shot_link_a"]
     * @param preciseShardingValue----分片键
     *             logicTableName 为逻辑表"short_link"
     *             columnName 分片健字段:code
     *             value 为从 SQL 中解析出的分片健的值，code对应的value为92AEva
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        // 获取逻辑表名
        String logicTableName = preciseShardingValue.getLogicTableName();

        //获取短链码
        String shortLink = preciseShardingValue.getValue();

        // 获取短链码最后一位，即表编码
        String tableSuffix = shortLink.substring(shortLink.length() - 1);

        return logicTableName + "_" + tableSuffix;
    }

    /**
     * 范围分片（时间、ID）
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<String> rangeShardingValue) {
        return Collections.emptyList();
    }


    @Override
    public void init() {

    }

    @Override
    public String getType() {
        return "";
    }
}
