package com.example.dcloud_account.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 产品表
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Product implements Serializable {
    /**
     * 主键 ID
     */
    private Long id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 详情
     */
    private String detail;

    /**
     * 图片
     */
    private String img;

    /**
     * 产品层级：FIRST 青铜、SECOND 黄金、THIRD 钻石
     */
    private String level;

    /**
     * 原价
     */
    private BigDecimal oldAmount;

    /**
     * 现价
     */
    private BigDecimal amount;

    /**
     * 工具类型：short_link、qrcode
     */
    private String pluginType;

    /**
     * 日次数：短链类型
     */
    private Integer dayTimes;

    /**
     * 总次数：活码才有
     */
    private Integer totalTimes;

    /**
     * 有效天数
     */
    private Integer validDay;

    /**
     * 最后修改时间
     */
    private Date gmtModified;

    /**
     * 创建时间
     */
    private Date gmtCreate;
}
