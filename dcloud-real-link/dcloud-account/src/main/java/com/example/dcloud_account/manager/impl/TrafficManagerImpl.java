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
    public Page<Traffic> pageAvailable(int page, int size, Long accountNo) {
        Page<Traffic> pageInfo = new Page<>(page, size);
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");

        LambdaQueryWrapper<Traffic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Traffic::getAccountNo, accountNo)
               .ge(Traffic::getExpiredDate, today)
               .orderByDesc(Traffic::getGmtCreate);

        return trafficMapper.selectPage(pageInfo, wrapper);
    }

    @Override
    public Traffic findByIdAndAccountNo(Long trafficId, Long accountNo) {
        LambdaQueryWrapper<Traffic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Traffic::getAccountNo, accountNo)
                .eq(Traffic::getId, trafficId);

        return trafficMapper.selectOne(queryWrapper);
    }

    /**
     * delete expired traffic.
     */
    @Override
    public int deleteExpiredTraffic() {
        LambdaQueryWrapper<Traffic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(Traffic::getExpiredDate, TimeUtil.format(new Date(), "yyyy-MM-dd"));

        return trafficMapper.delete(queryWrapper);
    }

    /**
     * add used times for traffic in one day.
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
