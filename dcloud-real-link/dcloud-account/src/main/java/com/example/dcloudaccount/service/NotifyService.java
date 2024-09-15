package com.example.dcloudaccount.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloudaccount.entity.Account;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
public interface NotifyService{

    /**
     * Test send SMS
     */
    void testNotify();
}
