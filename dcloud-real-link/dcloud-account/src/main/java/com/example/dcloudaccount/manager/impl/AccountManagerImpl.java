package com.example.dcloudaccount.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dcloudaccount.entity.Account;
import com.example.dcloudaccount.manager.AccountManager;
import com.example.dcloudaccount.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public int insert(Account account) {
        return accountMapper.insert(account);
    }

    @Override
    public List<Account> selectByPhone(String phone) {
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getPhone, phone);

        return accountMapper.selectList(queryWrapper);
    }
}
