package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("traffic")
public class Traffic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * Daily limit for short links
     */
    @TableField("day_limit")
    private Integer dayLimit;

    /**
     * Number of short links used for the day
     */
    @TableField("day_used")
    private Integer dayUsed;

    /**
     * Total limit, used for live codes
     */
    @TableField("total_limit")
    private Integer totalLimit;

    /**
     * Account number
     */
    @TableField("account_no")
    private Long accountNo;

    /**
     * Order number
     */
    @TableField("out_trade_no")
    private String outTradeNo;

    /**
     * Product level: FIRST(Bronze), SECOND(Gold), THIRD(Diamond)
     */
    @TableField("level")
    private String level;

    /**
     * Expiration date
     */
    @TableField("expired_date")
    private LocalDate expiredDate;

    /**
     * Plugin type
     */
    @TableField("plugin_type")
    private String pluginType;

    /**
     * Product primary key
     */
    @TableField("product_id")
    private Long productId;

    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    @TableField("gmt_modified")
    private LocalDateTime gmtModified;


}
