package com.example.dcloud_account.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dcloud_account.manager.TrafficTaskManager;
import com.example.dcloud_account.mapper.TrafficTaskMapper;
import com.example.dcloud_account.model.entity.TrafficTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class TrafficTaskManagerImpl implements TrafficTaskManager {

    @Resource
    private TrafficTaskMapper trafficTaskMapper;

    @Override
    public int add(TrafficTask trafficTask) {
        return trafficTaskMapper.insert(trafficTask);
    }

    @Override
    public TrafficTask findByIdAndAccountNo(Long id, Long accountNo) {
        LambdaQueryWrapper<TrafficTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrafficTask::getId,id)
                .eq(TrafficTask::getAccountNo,accountNo);

        return trafficTaskMapper.selectOne(wrapper);
    }

    @Override
    public int deleteByIdAndAccountNo(Long id, Long accountNo) {
        LambdaQueryWrapper<TrafficTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrafficTask::getId,id)
                .eq(TrafficTask::getAccountNo,accountNo);

        return trafficTaskMapper.delete(wrapper);
    }
}
