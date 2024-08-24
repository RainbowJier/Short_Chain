package com.example.dcloudaccount;

import com.example.dcloudaccount.component.SmsComponent;
import com.example.dcloudaccount.config.SmsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Description：Test Sms
 * @Author： RainbowJier
 * @Data： 2024/8/24 8:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DcloudAccountApplication.class)
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















