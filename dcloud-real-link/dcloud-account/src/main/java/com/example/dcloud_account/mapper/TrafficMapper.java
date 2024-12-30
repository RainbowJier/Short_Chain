package com.example.dcloud_account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dcloud_account.model.entity.Traffic;
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

    /**
     * Restore the used times the traffic has been used.
     */
    int restoreUsedTimes(@Param("accountNo") Long accountNo,
                         @Param("trafficId") Long trafficId,
                         @Param("usedTimes") Integer usedTimes,
                         @Param("useDateStr")String useDateStr);
}
