package com.example.dcloudcommon.config;

import com.example.dcloudcommon.enums.BizCodeEnum;
import com.example.dcloudcommon.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/10/18 16:45
 * @Version: 1.0
 */

@Slf4j
@Configuration
public class SnowFlakeWordIdConfig {
    /**
     * 动态指定 sharding jdbc 的雪花算法中的属性 work.id 属性
     * 通过调⽤ System.setProperty() 的⽅式实现,可用容器的 id 或者机器标识位
     * workId最⼤值 1L << 100，就是1024，即 0<= workId < 1024
     */
    static {
        try {
            // 获取本机IP地址
            InetAddress ip4 = Inet4Address.getLocalHost();
            String addressIp = ip4.getHostAddress();
            // 设定 workId 属性，计算IP地址的哈希码，并取绝对值，然后对1024取余，确保workId在0到1023之间
            String workId = (Math.abs(addressIp.hashCode()) % 1024) + "";
            System.setProperty("workerId",workId);
            log.info("SnowFlakeWordIdConfig workId: {}", workId);
        } catch (UnknownHostException e) {
            throw new
                    BizException(BizCodeEnum.OPS_NETWORK_ADDRESS_ERROR);
        }
    }
}
