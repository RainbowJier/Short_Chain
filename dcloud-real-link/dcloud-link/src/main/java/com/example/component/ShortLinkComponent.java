package com.example.component;

import com.example.dcloudcommon.util.CommonUtil;
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

        //转62进制
        return encodeToBase62(murmur32);
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
