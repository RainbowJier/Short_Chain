package com.example.dcloud_link.strategy;

import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.exception.BizException;
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
public class CustomDBPreciseShardingAlgorithm implements StandardShardingAlgorithm<String> {

    /**
     * 自定义分库算法
     * @param collection 数据源集合，例如：["ds0", "ds1", "dsa"]
     * @param preciseShardingValue----分片键
     *             logicTableName 为逻辑表：short_link
     *             columnName 分片健字段：code
     *             value 为从 SQL 中解析出的分片健的值，code对应的value为92AEva
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {

        //获取短链码第⼀位，即库编码
        String codePrefix = preciseShardingValue.getValue().substring(0, 1);

        for (String dsx : collection) {
            //获取库名的最后⼀位，真实配置的ds
            String targetNameSuffix = dsx.substring(dsx.length() - 1);

            //如果⼀致则返回
            if (codePrefix.equals(targetNameSuffix)){
                return dsx;
            }
        }

        throw new BizException(BizCodeEnum.DB_ROUTE_NOT_FOUND);
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
