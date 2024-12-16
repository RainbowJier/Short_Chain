package com.example.dcloud_account.job;

import com.example.dcloud_account.service.TrafficService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎任务调度处理器
 * @Date: 2024/12/13 17:28
 * @Version: 1.0
 */

@Component
@Slf4j
public class MyJobHandler {
    @Resource
    private TrafficService trafficService;

    @XxlJob(value = "trafficExpiredHandler", init = "init", destroy = "destroy")
    public ReturnT<String> execute(String param) {
        log.info("【trafficExpiredHandler】 execute success >>>>>");
        trafficService.deleteExpiredTraffic();

        return ReturnT.SUCCESS;
    }

    private void init() {
        log.info("【账号执行器】 MyJobHandler init >>>>>");
    }

    private void destroy() {
        log.info("【账号执行器】 MyJobHandler destroy>>>>>");
    }
}

