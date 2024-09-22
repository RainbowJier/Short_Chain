package com.example.dcloudaccount.service;

import com.example.dcloudcommon.enums.SendCodeEnum;
import com.example.dcloudcommon.util.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
public interface NotifyService{

    /**
     * send SMS
     */
    JsonData sendCode(SendCodeEnum userRegister, String to);
}
