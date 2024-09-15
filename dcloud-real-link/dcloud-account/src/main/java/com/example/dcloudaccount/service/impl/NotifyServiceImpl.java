package com.example.dcloudaccount.service.impl;

import com.example.dcloudaccount.entity.Account;
import com.example.dcloudaccount.service.NotifyService;
import com.example.dcloudcommon.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private RestTemplate restTemplate;

    /**
     * Test send SMS
     */
    @Override
    //@Async("threadPoolTaskExecutor")   // 自定义线程池
    @Transactional(rollbackFor = Exception.class)
    public void testNotify() {
        long beginTime = CommonUtil.getCurrentTimestamp();
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://old.xdclass.net", String.class);
        String body = forEntity.getBody();
        long endTime = CommonUtil.getCurrentTimestamp();
        log.info("The cost time = {},body={}",endTime-beginTime,body);
    }







}
