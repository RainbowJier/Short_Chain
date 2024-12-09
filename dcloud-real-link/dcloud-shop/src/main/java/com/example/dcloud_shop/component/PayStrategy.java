package com.example.dcloud_shop.component;

import com.example.dcloud_shop.entity.vo.PayInfoVo;


/**
 * 支付策略
 */
public interface PayStrategy {

    /**
     * 统一下单接口
     */
    String unifiedOrder(PayInfoVo payInfoVO);


    /**
     * 退款接口
     */
    String refund(PayInfoVo payInfoVo);


    /**
     * 查询支付状态
     */
    String queryPayStatus(PayInfoVo payInfoVo);


    /**
     * 关闭订单
     */
    String closeOrder(PayInfoVo payInfoVo);

}
