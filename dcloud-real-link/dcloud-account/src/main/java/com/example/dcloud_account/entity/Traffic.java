package com.example.dcloud_account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * (Traffic)实体类
 *
 * @author RainbowJier
 * @since 2024-12-11 17:02:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("traffic")
@Builder
public class Traffic implements Serializable {
    private Long id;
    /**
     * 每天限制多少条
     */
    private Integer dayLimit;
    /**
     * 当天用了多少条
     */
    private Integer dayUsed;
    /**
     * 总次数，活码采用
     */
    private Integer totalLimit;
    /**
     * 账号
     */
    private Long accountNo;
    /**
     * 订单号
     */
    private String outTradeNo;
    /**
     * Product level: FIRST(Bronze), SECOND(Gold), THIRD(Diamond)
     */
    private String level;
    /**
     * 过期日期
     */
    private Date expiredDate;
    /**
     * 插件类型
     */
    private String pluginType;
    /**
     * 商品主键
     */
    private Long productId;

    private Date gmtCreate;

    private Date gmtModified;

}

