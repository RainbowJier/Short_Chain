package com.example.dcloud_account.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_account.entity.Traffic;
import com.example.dcloud_account.manager.TrafficManager;
import com.example.dcloud_account.mapper.TrafficMapper;
import com.example.dcloud_common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

@Component
@Slf4j
public class TrafficManagerImpl implements TrafficManager {


    @Autowired
    private TrafficMapper trafficMapper;

    @Override
    public int add(Traffic traffic) {
        return trafficMapper.insert(traffic);
    }

    @Override
    public IPage<Traffic> pageAvailable(int page, int size, Long accountNo) {
        Page<Traffic> pageInfo = new Page<>(page, size);
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");

        LambdaQueryWrapper<Traffic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Traffic::getAccountNo, accountNo)
               .ge(Traffic::getExpiredDate, today)
               .orderByDesc(Traffic::getGmtCreate);

        return trafficMapper.selectPage(pageInfo, queryWrapper);
    }

    @Override
    public Traffic findByIdAndAccountNo(Long trafficId, Long accountNo) {
        LambdaQueryWrapper<Traffic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Traffic::getAccountNo, accountNo)
                .eq(Traffic::getId, trafficId);

        return trafficMapper.selectOne(queryWrapper);
    }

    /**
     * 给某个流量包增加天使用次数
     */
    @Override
    public int addDayUsedTimes(long trafficId, Long accountNo, int dayUsedTimes) {
        LambdaUpdateWrapper<Traffic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Traffic::getId, trafficId)
               .eq(Traffic::getAccountNo, accountNo)
               .set(Traffic::getDayUsed, dayUsedTimes);

        return trafficMapper.update(null, updateWrapper);
    }
}
