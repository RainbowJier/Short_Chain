package com.example.dcloud_account.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_account.entity.Traffic;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/12/11 16:45
 * @Version: 1.0
 */

public interface TrafficManager {

    /**
     * 新增流量包
     */
    int add(Traffic traffic);

    /**
     * 分页查询可用的流量包
     */
    Page<Traffic> pageAvailable(int page, int size, Long accountNo);

    /**
     * 查找详情
     */
    Traffic findByIdAndAccountNo(Long trafficId,Long accountNo);

    /**
     * delete expired traffic.
     */
    int deleteExpiredTraffic();

    /**
     * add used times for traffic in one day.
     */
    int addDayUsedTimes(long trafficId, Long accountNo, int dayUsedTimes);

}
