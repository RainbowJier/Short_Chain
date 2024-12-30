package com.example.dcloud_account.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_account.model.entity.Traffic;
import com.example.dcloud_account.manager.TrafficManager;
import com.example.dcloud_account.mapper.TrafficMapper;
import com.example.dcloud_common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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

    /**
     * find traffic detail by id and accountNo.
     */
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
    public int deleteExpiredTraffic(List<Long> trafficList) {
        LambdaQueryWrapper<Traffic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(Traffic::getExpiredDate, TimeUtil.format(new Date(), "yyyy-MM-dd"))
                .in(Traffic::getId, trafficList);

        return trafficMapper.delete(queryWrapper);
    }

    /**
     * add used times for traffic in one day.
     */
    @Override
    public int addDayUsedTimes(Long accountNo, Long trafficId, Integer dayUsed) {
        Traffic traffic = new Traffic()
                .setId(trafficId)
                .setAccountNo(accountNo)
                .setDayUsed(dayUsed);
        return trafficMapper.addDayUsedTimes(traffic);
    }

    /**
     * get valid traffic list by accountNo.
     * <p>
     * select * from traffic where account_no = ? and (expired_date >= ? or out_trade_no=free_init)
     */
    @Override
    public List<Traffic> selectAvailableTraffics(Long accountNo) {
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");

        LambdaQueryWrapper<Traffic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Traffic::getAccountNo, accountNo);
        queryWrapper.and(wrapper -> wrapper
                .ge(Traffic::getExpiredDate, today)
                .or()
                .eq(Traffic::getOutTradeNo, "free_init"));

        return trafficMapper.selectList(queryWrapper);
    }

    /**
     * batch update used times for traffic.
     */
    @Override
    public int batchUpdateUsedTimes(Long accountNo,List<Long> unUpdatedTrafficIds) {
        LambdaUpdateWrapper<Traffic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Traffic::getId,unUpdatedTrafficIds)
                .eq(Traffic::getAccountNo,accountNo)
                .set(Traffic::getDayUsed, 0)
                .set(Traffic::getGmtModified, new Date());

        return trafficMapper.update(null, updateWrapper);
    }

    /**
     * get random traffic list.
     */
    @Override
    public List<Traffic> selectRandomTraffics(int randomCount) {
        return trafficMapper.selectRandomTraffics(randomCount);
    }

    @Override
    public int restoreUsedTimes(Long accountNo, Long trafficId, Integer usedTimes, String useDateStr) {
        return trafficMapper.restoreUsedTimes(accountNo,trafficId,usedTimes,useDateStr);
    }
}
