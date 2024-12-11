package com.example.dcloud_account.service;


import com.example.dcloud_common.entity.EventMessage;

public interface TrafficService {

    /**
     * 流量包发放
     */
    void handleTrafficMessage(EventMessage eventMessage);
}
