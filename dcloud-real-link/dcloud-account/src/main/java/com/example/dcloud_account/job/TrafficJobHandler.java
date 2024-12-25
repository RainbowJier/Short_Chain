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

@Slf4j
@Component
public class TrafficJobHandler {

    @Resource
    private TrafficService trafficService;

    private void init() {
        log.info("【TrafficJobHandler】 init >>>>>");
    }

    @XxlJob(value = "trafficExpiredHandler", init = "init", destroy = "destroy")
    public ReturnT<String> execute(String param) {
        log.info("=================【trafficExpiredHandler】=================");
        log.info("=================Start execute=================");
        trafficService.deleteExpiredTraffic();
        log.info("=================End execute=================");
        return ReturnT.SUCCESS;
    }

    private void destroy() {
        log.info("【TrafficJobHandler】destroy>>>>>");
    }
}

