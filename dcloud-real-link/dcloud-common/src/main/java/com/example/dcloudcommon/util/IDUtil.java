package com.example.dcloudcommon.util;

import org.apache.shardingsphere.sharding.algorithm.keygen.SnowflakeKeyGenerateAlgorithm;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎雪花算法生成唯一ID
 * @Date: 2024/10/18 17:10
 * @Version: 1.0
 */

public class IDUtil {


    private static SnowflakeKeyGenerateAlgorithm shardingKeyGenerator = new SnowflakeKeyGenerateAlgorithm();
    /**
     * 雪花算法⽣成器,配置workId，避免重复
     *
     * 10进制 654334919987691526
     * 64位
     0000100100010100101010100010010010010110000000000000
     000000000110
     */
    public static Comparable<?> generateSnowFlakeID(){
        return shardingKeyGenerator.generateKey();
    }
}
