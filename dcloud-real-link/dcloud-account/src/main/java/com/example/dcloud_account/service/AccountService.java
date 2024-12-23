package com.example.dcloud_account.service;

import com.example.dcloud_account.controller.request.AccountLoginRequest;
import com.example.dcloud_account.controller.request.AccountRegisterRequest;
import com.example.dcloud_common.util.JsonData;

/**
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
public interface AccountService{

    /**
     * register
     */
    JsonData register(AccountRegisterRequest accountRegisterRequest);


    /**
     * 用户登录
     */
    JsonData login(AccountLoginRequest accountLoginRequest);
}
