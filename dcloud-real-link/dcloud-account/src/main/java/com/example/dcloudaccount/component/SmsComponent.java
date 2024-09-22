package com.example.dcloudaccount.component;

import com.example.dcloudaccount.config.SmsConfig;
import com.example.dcloudcommon.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Description：TODO
 * @Author： RainbowJier
 * @Data： 2024/8/24 11:45
 */

@Slf4j
@Component
public class SmsComponent {

    private static final String URL_TEMPLATE = "https://jmsms.market.alicloudapi.com/sms/send?mobile=%s&templateId=%s&value=%s";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private SmsConfig smsConfig;

    /**
     * Send verification code by SMS.
     * @param to : Phone number
     * @param templateId : SMS template ID
     * @param value : Verification code
     */
    @Async("threadPoolTaskExecutor")
    public void send(String to, String templateId,String value) {
        // Millisecond timestamp
        Long begin = CommonUtil.getCurrentTimestamp();

        String url = String.format(URL_TEMPLATE, to, templateId, value);

        // Create headers entity
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "APPCODE " + smsConfig.getAppCode());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send request and get response.
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        Long end = CommonUtil.getCurrentTimestamp();

        log.info("Send SMS, cost time:{}ms, url={},body={}", end - begin, url, response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Send SMS success, response message:{}", response.getBody());
        } else {
            log.error("Send SMS failed, status code:{}", response.getBody());
        }

    }
}
