package com.example.dcloud_account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dcloud_account.entity.Traffic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TrafficMapper extends BaseMapper<Traffic> {

    /**
     * add day used times
     */
    int addDayUsedTimes(Traffic traffic);

    /**
     * select random traffic
     */
    List<Traffic> selectRandomTraffics(int randomCount);
}
