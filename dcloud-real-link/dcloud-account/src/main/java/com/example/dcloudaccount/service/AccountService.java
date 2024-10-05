package com.example.dcloudaccount.service;

import com.example.dcloudaccount.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloudaccount.entity.request.AccountLoginRequest;
import com.example.dcloudaccount.entity.request.AccountRegisterRequest;
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
