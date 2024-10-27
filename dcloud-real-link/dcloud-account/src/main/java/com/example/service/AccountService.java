package com.example.service;

import com.example.entity.request.AccountLoginRequest;
import com.example.entity.request.AccountRegisterRequest;
import com.example.dcloudcommon.util.JsonData;

/**
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
public interface AccountService{

    /**
     * 用户注册
     */
    JsonData register(AccountRegisterRequest accountRegisterRequest);


    /**
     * 用户登录
     */
    JsonData login(AccountLoginRequest accountLoginRequest);
}
