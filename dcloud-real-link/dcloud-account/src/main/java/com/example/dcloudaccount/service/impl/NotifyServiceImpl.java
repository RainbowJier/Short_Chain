package com.example.dcloudaccount.service.impl;

import com.example.dcloudaccount.component.SmsComponent;
import com.example.dcloudaccount.config.SmsConfig;
import com.example.dcloudaccount.service.NotifyService;
import com.example.dcloudcommon.enums.SendCodeEnum;
import com.example.dcloudcommon.util.CheckUtil;
import com.example.dcloudcommon.util.CommonUtil;
import com.example.dcloudcommon.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private SmsComponent smsComponent;

    /**
     * Test send SMS
     */
    @Override
    public JsonData sendCode(SendCodeEnum userRegister, String to) {
        // Phone or Email
        if(CheckUtil.isPhone(to)){
            String templateId = smsConfig.getTemplateId();

            // Generate random code.
            String randomCode = CommonUtil.getRandomCode(6);

            // Send SMS
            smsComponent.send(to, templateId, randomCode);
        }
        if (CheckUtil.isEmail(to)){

        }

        return JsonData.buildSuccess();
    }
}
