package com.example.dcloud_account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloud_account.config.RabbitMQConfig;
import com.example.dcloud_account.controller.request.AccountLoginRequest;
import com.example.dcloud_account.controller.request.AccountRegisterRequest;
import com.example.dcloud_account.entity.Account;
import com.example.dcloud_account.manager.AccountManager;
import com.example.dcloud_account.mapper.AccountMapper;
import com.example.dcloud_account.service.AccountService;
import com.example.dcloud_account.service.NotifyService;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.entity.LoginUser;
import com.example.dcloud_common.enums.AuthTypeEnum;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.EventMessageType;
import com.example.dcloud_common.enums.SendCodeEnum;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.IDUtil;
import com.example.dcloud_common.util.JWTUtil;
import com.example.dcloud_common.util.JsonData;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
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

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitMQConfig rabbitMQConfig;

    /**
     * 免费流量包商品id
     */
    private static final Long FREE_TRAFFIC_PRODUCT_ID = 1L;


    /**
     * 用户注册
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonData register(AccountRegisterRequest accountRegisterRequest) {
        boolean checkCode;
        String phone = accountRegisterRequest.getPhone();
        String code = accountRegisterRequest.getCode();

        // check if the verification code is correct.
        if (StringUtil.isNotBlank(phone)) {
            checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, phone, code);

            // incorrect.
            if (!checkCode) {
                return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
            }
        }

        Account account = new Account();
        BeanUtils.copyProperties(accountRegisterRequest, account);

        // generate accountNo that is unique.
        account.setAccountNo((Long) IDUtil.generateSnowFlakeID());
        account.setAuth(AuthTypeEnum.DEFAULT.name());

        // set password salt.
        account.setSecret("$1$" + CommonUtil.getStringNumRandom(8));

        // encrypt password.
        String encryptedPassword = Md5Crypt.md5Crypt(accountRegisterRequest.getPwd().getBytes(), account.getSecret());
        account.setPwd(encryptedPassword);

        try{
            int insertRow = accountManager.insert(account);
            if(insertRow < 1){
                return JsonData.buildResult(BizCodeEnum.PHONE_REPEAT);
            }
            log.info("rows:{}，register success：{}", insertRow, account);

            // add free traffic for new user.
            userRegisterInitTask(account);
            return JsonData.buildSuccess();
        }catch (Exception e){
            throw new RuntimeException(BizCodeEnum.PHONE_REPEAT.getMessage());
        }
    }

    private void userRegisterInitTask(Account account) {
        EventMessage eventMessage = EventMessage.builder()
                .bizId(FREE_TRAFFIC_PRODUCT_ID.toString())
                .accountNo(account.getAccountNo())
                .eventMessageType(EventMessageType.TRAFFIC_FREE_INIT.name())
                .build();

        rabbitTemplate.convertAndSend(rabbitMQConfig.getTrafficEventExchange(),
                rabbitMQConfig.getTrafficFreeInitRoutingKey(), eventMessage);

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
                HashMap<String, Object> data = new HashMap<>();
                data.put("token", token);
                return JsonData.buildSuccess(data, "登录成功");
            } else {
                // 密码错误
                return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
        } else {
            // 密码错误
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
    }



}
