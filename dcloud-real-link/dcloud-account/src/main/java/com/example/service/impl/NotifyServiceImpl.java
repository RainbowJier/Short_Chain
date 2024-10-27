package com.example.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.example.component.SmsComponent;
import com.example.config.SmsConfig;
import com.example.service.NotifyService;
import com.example.dcloudcommon.constant.RedisKey;
import com.example.dcloudcommon.enums.BizCodeEnum;
import com.example.dcloudcommon.enums.SendCodeEnum;
import com.example.dcloudcommon.util.CheckUtil;
import com.example.dcloudcommon.util.CommonUtil;
import com.example.dcloudcommon.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 服务实现类
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int CODE_EXPIRE = 60 * 1000 * 10;

    /**
     * Send SMS code.
     *
     * @param sendCodeEnum : Login or Register
     * @param to           : Phone or Email
     */
    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {

        // Get the code from Redis.
        String cacheKey = String.format(RedisKey.CHECK_CODE_KEY, sendCodeEnum.name(), to);
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);

        // Check if the code has been sent within 60 seconds.
        if (StringUtils.isNotBlank(cacheValue)) {
            Long ttl = Long.parseLong(cacheValue.split("_")[1]);
            Long currentTimeStamp = CommonUtil.getCurrentTimestamp();
            long leftTime = currentTimeStamp - ttl;

            if (leftTime < (1000 * 60)) {
                log.info("The code has been sent to the phone or email within 60 seconds, " +
                        "the remaining time is {} seconds", leftTime);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }

        // Store the code in Redis and set the expiration time to 10 minutes.
        // Generate random code.
        String randomCode = CommonUtil.getRandomCode(6);
        String value = randomCode + "_" + CommonUtil.getCurrentTimestamp();
        redisTemplate.opsForValue().set(cacheKey, value, CODE_EXPIRE, TimeUnit.MILLISECONDS);

        // Phone
        if (CheckUtil.isPhone(to)) {
            // Send SMS
            smsComponent.send(to, smsConfig.getTemplateId(), randomCode);
        }

        // Email
        if (CheckUtil.isEmail(to)) {

        }

        return JsonData.buildSuccess();
    }


    /**
     * 校验验证码
     */
    @Override
    public boolean checkCode(SendCodeEnum sendCodeEnum, String to, String code) {
        // Get the code from Redis.
        String cacheKey = String.format(RedisKey.CHECK_CODE_KEY, sendCodeEnum.name(), to);
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);

        if(StringUtils.isNotBlank(cacheValue)){
            String cacheCode = cacheValue.split("_")[0];
            if(cacheCode.equalsIgnoreCase(code)){
                // 删除验证码
                redisTemplate.delete(cacheKey);
                return true;
            }
        }

        return false;
    }
}
