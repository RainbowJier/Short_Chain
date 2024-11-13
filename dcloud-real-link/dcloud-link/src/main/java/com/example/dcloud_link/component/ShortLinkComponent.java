package com.example.dcloud_link.component;

import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_link.strategy.ShardingDBConfig;
import com.example.dcloud_link.strategy.ShardingTableConfig;
import org.springframework.stereotype.Component;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/10/27 16:05
 */


@Component
public class ShortLinkComponent {

    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 生成短链码
     *
     * @return db编码+6位短链编码
     */
    public String createShortLinkCode(String originalUrl) {
        long murmur32 = CommonUtil.murmurHash32(originalUrl);

        String code = encodeToBase62(murmur32);

        // 随机库前缀
        String dbPrefix = ShardingDBConfig.getRandomDBPrefix(code);

        // 随机表后缀
        String tableSubfix = ShardingTableConfig.getRandomTableSubfix(code);

        //转62进制
        return dbPrefix + code + tableSubfix;
    }

    /**
     * 将短链码（10进制）转62进制
     */
    private static String encodeToBase62(long num) {
        // StringBuilder：线程不安全与 StringBuffer 的对比使用
        StringBuilder sb = new StringBuilder();
        if (num < 0) {
            throw new IllegalArgumentException("输入的数字不能为负数");
        }
        do {
            int i = (int) (num % 62);
            sb.append(CHARS.charAt(i));
            num /= 62;
        } while (num > 0);
        return sb.reverse().toString();
    }
}
