package com.example.dcloud_account.service;


import com.example.dcloud_account.controller.request.TrafficPageRequest;
import com.example.dcloud_account.controller.request.UseTrafficRequest;
import com.example.dcloud_account.model.vo.TrafficVo;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.util.JsonData;

import java.util.Map;

public interface TrafficService {

    void handleTrafficMessage(EventMessage eventMessage);

    /**
     * Get available traffic list by page.
     */
    Map<String, Object> pageAvailable(TrafficPageRequest request);

    /**
     * Get traffic detail by id.
     */
    TrafficVo detail(long trafficId);

    /**
     * delete expired traffic.
     */
    boolean deleteExpiredTraffic();

    /**
     * reduce traffic.
     */
    JsonData reduce(UseTrafficRequest useTrafficRequest);
}
