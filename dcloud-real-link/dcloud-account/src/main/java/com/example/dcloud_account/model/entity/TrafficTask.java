package com.example.dcloud_account.model.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (TrafficTask)表实体类
 *
 * @author RainbowJier
 * @since 2024-12-29 14:37:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("traffic_task")
@Builder
public class TrafficTask extends Model<TrafficTask> {

    private Long id;

    private Long accountNo;

    private Long trafficId;

    private Integer useTimes;
    //Lock state: LOCK, FINISH, CANCEL
    private String lockState;
    //Unique identifier
    private String bizId;

    private Date gmtCreate;

    private Date gmtModified;
}

