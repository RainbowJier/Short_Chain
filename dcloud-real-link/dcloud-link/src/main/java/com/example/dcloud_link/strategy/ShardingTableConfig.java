package com.example.dcloud_link.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description：随机表编码
 * @Author： RainbowJier
 * @Data： 2024/11/2 16:21
 */
public class ShardingTableConfig {
    private static final List<String> tabletSubfixList = new ArrayList<String>();

    private static final Random random = new Random();

    // 配置启用的表编码
    static {
        tabletSubfixList.add("0");
        tabletSubfixList.add("a");
    }

    /**
     * 随机获取库编码
     */
    public static String getRandomTableSubfix(){
        int index = random.nextInt(tabletSubfixList.size());
        return tabletSubfixList.get(index);
    }
}
