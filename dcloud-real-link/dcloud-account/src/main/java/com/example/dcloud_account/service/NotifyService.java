package com.example.dcloud_account.service;

import com.example.dcloud_common.enums.SendCodeEnum;
import com.example.dcloud_common.util.JsonData;

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


    /**
     * 校验验证码
     */
    boolean checkCode(SendCodeEnum sendCodeEnum, String to,String code);


}
