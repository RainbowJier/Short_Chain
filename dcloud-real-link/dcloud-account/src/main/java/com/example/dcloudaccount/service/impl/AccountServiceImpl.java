package com.example.dcloudaccount.service.impl;

import com.example.dcloudaccount.entity.Account;
import com.example.dcloudaccount.mapper.AccountMapper;
import com.example.dcloudaccount.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

}
