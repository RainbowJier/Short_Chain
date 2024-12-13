package com.example.dcloud_account.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎任务调度处理器
 * @Date: 2024/12/13 17:28
 * @Version: 1.0
 */

@Component
@Slf4j
public class MyJobHandler {
    @XxlJob(value = "trafficJobHandler", init = "init", destroy = "destroy")
    public ReturnT<String> execute(String param) {
        log.info("【账号执行器】 execute 任务⽅法触发成功");
        return ReturnT.SUCCESS;
    }

    private void init() {
        log.info("【账号执行器】 MyJobHandler init >>>>>");
    }

    private void destroy() {
        log.info("【账号执行器】 MyJobHandler destroy>>>>>");
    }
}

