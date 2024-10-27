package com.example.controller;


import com.example.entity.request.AccountLoginRequest;
import com.example.entity.request.AccountRegisterRequest;
import com.example.service.AccountService;
import com.example.dcloudcommon.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
@RestController
@RequestMapping("/api/account/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 注册接口
     */
    @PostMapping("register")
    public JsonData register(@RequestBody AccountRegisterRequest accountRegisterRequest){
        return accountService.register(accountRegisterRequest);
    }

    /**
     * 登录接口
     */
    @PostMapping("login")
    public JsonData login(@RequestBody AccountLoginRequest accountLoginRequest){
        return accountService.login(accountLoginRequest);
    }

}
