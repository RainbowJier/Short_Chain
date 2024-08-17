package com.example.dcloudcommon.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author RainbowJier
 * @since 2024-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("traffic_task")
@ApiModel(value="TrafficTask对象", description="")
public class TrafficTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long accountNo;

    private Long trafficId;

    private Integer useTimes;

    @ApiModelProperty(value = "Lock state: LOCK, FINISH, CANCEL")
    private String lockState;

    @ApiModelProperty(value = "Unique identifier")
    private String messageId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
