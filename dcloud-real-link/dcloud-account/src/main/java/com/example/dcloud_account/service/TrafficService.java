package com.example.dcloud_account.service;


import com.example.dcloud_account.controller.request.TrafficPageRequest;
import com.example.dcloud_account.entity.vo.TrafficVo;
import com.example.dcloud_common.entity.EventMessage;

import java.util.Map;

public interface TrafficService {

    /**
     * 流量包发放
     */
    void handleTrafficMessage(EventMessage eventMessage);

    /**
     * 查询可用流量包列表
     */
    Map<String, Object> pageAvailable(TrafficPageRequest request);

    /**
     * 查询某个流量包的详情
     */
    TrafficVo detail(long trafficId);

    /**
     * delete expired traffic.
     */
    boolean deleteExpiredTraffic();
}
