package com.example.dcloud_account.manager;

import com.example.dcloud_account.model.entity.TrafficTask;

/**
 * (TrafficTask)表服务接口
 *
 * @author RainbowJier
 * @since 2024-12-29 14:46:19
 */
public interface TrafficTaskManager {

    int add(TrafficTask trafficTask);

    TrafficTask findByIdAndAccountNo(Long id, Long accountNo);

    int deleteByIdAndAccountNo(Long id, Long accountNo);

}

