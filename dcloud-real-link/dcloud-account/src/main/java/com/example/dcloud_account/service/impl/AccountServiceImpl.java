package com.example.dcloud_account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud_account.manager.AccountManager;
import com.example.dcloud_account.service.AccountService;
import com.example.dcloud_account.service.NotifyService;
import com.example.dcloud_account.controller.request.AccountLoginRequest;
import com.example.dcloud_account.controller.request.AccountRegisterRequest;
import com.example.dcloud_account.entity.Account;
import com.example.dcloud_account.mapper.AccountMapper;
import com.example.dcloud_common.enums.AuthTypeEnum;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.SendCodeEnum;
import com.example.dcloud_common.entity.LoginUser;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.IDUtil;
import com.example.dcloud_common.util.JWTUtil;
import com.example.dcloud_common.util.JsonData;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author RainbowJier
 * @since 2024-08-17
 */
@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private NotifyService notifyService;


    @Autowired
    private AccountManager accountManager;


    /**
     * 用户注册
     */
    @Override
    @Transactional
    public JsonData register(AccountRegisterRequest accountRegisterRequest) {
        boolean checkCode;
        String phone = accountRegisterRequest.getPhone();
        String code = accountRegisterRequest.getCode();

        // 判断验证码是否正确
        if (StringUtil.isNotBlank(phone)) {
            checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, phone, code);

            // 验证码错误
            if (!checkCode) {
                return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
            }
        }

        Account account = new Account();
        BeanUtils.copyProperties(accountRegisterRequest, account);

        // 生成账号唯一编号
        account.setAccountNo((Long) IDUtil.generateSnowFlakeID());

        // 设置用户级别
        account.setAuth(AuthTypeEnum.DEFAULT.name());

        // 设置密码加密的“秘钥”
        account.setSecret("$1$" + CommonUtil.getStringNumRandom(8));
        String encryptedPassword = Md5Crypt.md5Crypt(accountRegisterRequest.getPwd().getBytes(), account.getSecret());
        // 设置加密后的密码
        account.setPwd(encryptedPassword);

        // 新增用户
        int insertRow = accountManager.insert(account);
        log.info("rows:{}，注册成功：{}", insertRow, account);

        // 用户注册成功，新用户发放福利 TODO
        userRegisterInitTask(account);

        return JsonData.buildSuccess();
    }

    /**
     * 用户登录
     */
    @Override
    public JsonData login(AccountLoginRequest accountLoginRequest) {
        // 根据手机号查找账号
        List<Account> accountList = accountManager.selectByPhone(accountLoginRequest.getPhone());

        if (accountList.size() == 1) {
            Account account = accountList.get(0);
            String secret = account.getSecret();
            String loginPwd = accountLoginRequest.getPwd();
            String encryptedPwd = Md5Crypt.md5Crypt(loginPwd.getBytes(), secret);

            // 密码校验
            if (encryptedPwd.equalsIgnoreCase(account.getPwd())) {
                // 密码正确
                // 封装login user
                LoginUser loginUser = LoginUser.builder().build();
                BeanUtils.copyProperties(account, loginUser);

                // 生成token
                String token = JWTUtil.generateJsonToken(loginUser);

                return JsonData.buildSuccess(token);
            } else {
                // 密码错误
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
        } else {
            // 密码错误
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
    }


    /**
     * 用户初始化，发放福利：流量包   TODO
     */
    private void userRegisterInitTask(Account account) {

    }

}
