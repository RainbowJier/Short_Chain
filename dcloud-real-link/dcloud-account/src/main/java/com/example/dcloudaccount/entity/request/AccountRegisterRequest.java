package com.example.dcloudaccount.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎账号注册实体类
 * @Date: 2024/10/5 14:28
 * @Version: 1.0
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterRequest {

    /**
     * 头像
     */
    private String headImg;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 用户名
     */
    private String username;

    /**
     * 验证码
     */
    private String code;

}
