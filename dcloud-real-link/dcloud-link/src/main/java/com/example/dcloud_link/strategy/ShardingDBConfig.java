package com.example.dcloud_link.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description：获取随机库编码
 * @Author： RainbowJier
 * @Data： 2024/11/2 16:21
 */
public class ShardingDBConfig {
    private static final List<String> dbPrefixList = new ArrayList<String>();

    private static final Random random = new Random();

    // 配置启用的库编码
    static {
        dbPrefixList.add("0");
        dbPrefixList.add("1");
        dbPrefixList.add("a");
    }

    /**
     * 随机获取库编码
     */
    public static String getRandomDBPrefix(){
        int index = random.nextInt(dbPrefixList.size());
        return dbPrefixList.get(index);
    }

}
