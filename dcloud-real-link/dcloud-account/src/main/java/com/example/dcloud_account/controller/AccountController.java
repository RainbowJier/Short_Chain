package com.example.dcloud_account.controller;



import com.example.dcloud_account.service.AccountService;
import com.example.dcloud_account.controller.request.AccountLoginRequest;
import com.example.dcloud_account.controller.request.AccountRegisterRequest;
import com.example.dcloud_common.util.JsonData;
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
