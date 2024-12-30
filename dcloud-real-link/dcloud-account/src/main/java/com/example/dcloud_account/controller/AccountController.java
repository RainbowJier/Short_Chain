package com.example.dcloud_account.controller;

import com.example.dcloud_account.controller.request.AccountLoginRequest;
import com.example.dcloud_account.controller.request.AccountRegisterRequest;
import com.example.dcloud_account.service.AccountService;
import com.example.dcloud_common.util.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/account/v1")
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * register
     * <p>
     * register user will get the free traffic.
     */
    @PostMapping("register")
    public JsonData register(@RequestBody AccountRegisterRequest accountRegisterRequest) {
        return accountService.register(accountRegisterRequest);
    }

    @PostMapping("login")
    public JsonData login(@RequestBody AccountLoginRequest accountLoginRequest) {
        return accountService.login(accountLoginRequest);
    }


}
