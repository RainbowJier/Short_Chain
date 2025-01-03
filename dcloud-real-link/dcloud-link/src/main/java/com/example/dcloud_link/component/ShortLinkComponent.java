package com.example.dcloud_link.component;

import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_link.strategy.ShardingDBConfig;
import com.example.dcloud_link.strategy.ShardingTableConfig;
import org.springframework.stereotype.Component;

@Component
public class ShortLinkComponent {

    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Generate a short link code based on the original URL.
     * Format: dbPrefix + code + tableSuffix
     */
    public String createShortLinkCode(String originalUrl) {
        long murmur32 = CommonUtil.murmurHash32(originalUrl);
        String code = encodeToBase62(murmur32);

        String dbPrefix = ShardingDBConfig.getHashDBPrefix(code);
        String tableSuffix = ShardingTableConfig.getHashTableSuffix(code);

        return dbPrefix + code + tableSuffix;
    }

    /**
     * Convert a 10-digit string to a 62-digit string.
     */
    private static String encodeToBase62(long num) {
        StringBuilder sb = new StringBuilder();
        if (num < 0) {
            throw new IllegalArgumentException("The number must be non-negative.");
        }
        do {
            int i = (int) (num % 62);
            sb.append(CHARS.charAt(i));
            num /= 62;
        } while (num > 0);
        return sb.reverse().toString();
    }
}
