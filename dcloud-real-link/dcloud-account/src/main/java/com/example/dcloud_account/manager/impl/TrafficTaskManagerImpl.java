package com.example.dcloud_account.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dcloud_account.manager.TrafficTaskManager;
import com.example.dcloud_account.mapper.TrafficTaskMapper;
import com.example.dcloud_account.model.entity.TrafficTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * (TrafficTask)表服务实现类
 *
 * @author RainbowJier
 * @since 2024-12-29 14:46:20
 */
@Component
public class TrafficTaskManagerImpl implements TrafficTaskManager {

    @Autowired
    private TrafficTaskMapper trafficTaskMapper;


    @Override
    public int add(TrafficTask trafficTask) {
        return trafficTaskMapper.insert(trafficTask);
    }

    @Override
    public TrafficTask findByIdAndAccountNo(Long id, Long accountNo) {
        LambdaQueryWrapper<TrafficTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrafficTask::getId, id)
                .eq(TrafficTask::getAccountNo, accountNo);

        return trafficTaskMapper.selectOne(wrapper);
    }

    @Override
    public int deleteByIdAndAccountNo(Long id, Long accountNo) {
        LambdaQueryWrapper<TrafficTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrafficTask::getId, id)
                .eq(TrafficTask::getAccountNo, accountNo);

        return trafficTaskMapper.delete(wrapper);
    }
}

