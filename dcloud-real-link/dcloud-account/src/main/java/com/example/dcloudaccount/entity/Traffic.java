package com.example.dcloudaccount.entity;

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
 * @author RainbowJier
 * @since 2024-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("traffic")
public class Traffic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Daily limit for short links
     */
    private Integer dayLimit;

    /**
     * Number of short links used for the day
     */
    private Integer dayUsed;

    /**
     * Total limit, used for live codes
     */
    private Integer totalLimit;

    /**
     * Account number
     */
    private Long accountNo;

    /**
     * Order number
     */
    private String outTradeNo;

    /**
     * Product level: FIRST(Bronze), SECOND(Gold), THIRD(Diamond)
     */
    private String level;

    /**
     * Expiration date
     */
    private LocalDate expiredDate;

    /**
     * Plugin type
     */
    private String pluginType;

    /**
     * Product primary key
     */
    private Long productId;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
