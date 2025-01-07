package com.example.dcloud_account;

import com.example.dcloud_account.component.SmsComponent;
import com.example.dcloud_account.config.SmsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description：Test Sms
 * @Author： RainbowJier
 * @Data： 2024/8/24 8:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApplication.class)
public class SmsTest {

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private SmsComponent smsComponent;


    @Test
    public void testSendSms() {
        String templateId = smsConfig.getTemplateId();
        smsComponent.send("13599829312",templateId, "1234");
    }
}















