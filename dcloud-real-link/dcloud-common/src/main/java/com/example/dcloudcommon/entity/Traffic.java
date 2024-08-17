package com.example.dcloudcommon.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
@TableName("traffic")
@ApiModel(value="Traffic对象", description="")
public class Traffic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "Daily limit for short links")
    private Integer dayLimit;

    @ApiModelProperty(value = "Number of short links used for the day")
    private Integer dayUsed;

    @ApiModelProperty(value = "Total limit, used for live codes")
    private Integer totalLimit;

    @ApiModelProperty(value = "Account number")
    private Long accountNo;

    @ApiModelProperty(value = "Order number")
    private String outTradeNo;

    @ApiModelProperty(value = "Product level: FIRST(Bronze), SECOND(Gold), THIRD(Diamond)")
    private String level;

    @ApiModelProperty(value = "Expiration date")
    private LocalDate expiredDate;

    @ApiModelProperty(value = "Plugin type")
    private String pluginType;

    @ApiModelProperty(value = "Product primary key")
    private Long productId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
