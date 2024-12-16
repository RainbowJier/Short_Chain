package com.example.dcloud_account.entity.vo;

import com.example.dcloud_account.entity.Traffic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/12/16 16:17
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseTrafficVo {
    /**
     * today's useful times = total times - used times
     */
    private Integer dayTotalLeftTimes;

    private Traffic currentTraffic;

    private List<Long> unUpdatedTrafficIds;

}
