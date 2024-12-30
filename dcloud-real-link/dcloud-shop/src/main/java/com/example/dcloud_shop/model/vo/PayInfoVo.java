package com.example.dcloud_shop.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/11/26 11:09
 * @Version: 1.0
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PayInfoVo {

    private String outTradeNo;

    /**
     * 订单总金额，单位：分
     */
    private BigDecimal payFee;

    /**
     * 支付类型：alipay、wechat、bank
     */
    private String payType;

    /**
     * 客户端类型：web、app、h5
     */
    private String clientType;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 订单支付超时，单位：毫秒
     */
    private Long orderPayTimeoutMills;

    /**
     * 用户标识，账号
     */
    private Long accountNo;
}
