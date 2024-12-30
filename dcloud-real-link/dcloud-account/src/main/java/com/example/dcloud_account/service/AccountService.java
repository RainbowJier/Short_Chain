package com.example.dcloud_account.service;

import com.example.dcloud_account.controller.request.AccountLoginRequest;
import com.example.dcloud_account.controller.request.AccountRegisterRequest;
import com.example.dcloud_common.util.JsonData;


public interface AccountService{

    JsonData register(AccountRegisterRequest accountRegisterRequest);

    JsonData login(AccountLoginRequest accountLoginRequest);
}
