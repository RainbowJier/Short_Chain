package com.example.dcloud_account.manager;


import com.example.dcloud_account.model.entity.TrafficTask;

public interface TrafficTaskManager {

    int add(TrafficTask trafficTask);

    TrafficTask findByIdAndAccountNo(Long id,Long accountNo);

    int deleteByIdAndAccountNo(Long id,Long accountNo);

}
