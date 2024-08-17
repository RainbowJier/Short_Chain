package com.example.dcloudaccount.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloudaccount.entity.Account;
import com.example.dcloudaccount.manager.AccountManager;
import com.example.dcloudaccount.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-08-17
 */

@Component
@Slf4j
public class AccountManagerImpl extends ServiceImpl<AccountMapper, Account> implements AccountManager {

}
