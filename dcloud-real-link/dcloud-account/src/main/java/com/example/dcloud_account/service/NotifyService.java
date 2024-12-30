package com.example.dcloud_account.service;

import com.example.dcloud_common.enums.SendCodeEnum;
import com.example.dcloud_common.util.JsonData;

public interface NotifyService{

    /**
     * send SMS
     */
    JsonData sendCode(SendCodeEnum userRegister, String to);


    /**
     * check SMS code
     */
    boolean checkCode(SendCodeEnum sendCodeEnum, String to,String code);


}
