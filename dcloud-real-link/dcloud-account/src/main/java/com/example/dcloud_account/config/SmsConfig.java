package com.example.dcloud_account.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/8/24 11:44
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
    private String appCode;
    private String templateId;
}
