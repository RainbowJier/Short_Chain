package com.example.dcloudcommon.service.impl;

import com.example.dcloudcommon.entity.Account;
import com.example.dcloudcommon.mapper.AccountMapper;
import com.example.dcloudcommon.service.IAccountService;
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
