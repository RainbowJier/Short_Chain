package com.example.dcloud_shop.component;

import com.example.dcloud_shop.entity.vo.PayInfoVo;


/**
 * 支付策略上下文，根据不同的支付方式，选择不同的支付策略进行支付操作
 */
public class PayStrategyContext {

    private PayStrategy payStrategy;

    public PayStrategyContext(PayStrategy payStrategy) {
        this.payStrategy = payStrategy;
    }

    /**
     * 根据策略对象，执行不同的下单接口
     */
    public String executeUnifiedOrder(PayInfoVo payInfoVo) {
        return payStrategy.unifiedOrder(payInfoVo);
    }


    /**
     * 根据策略对象，执行不同的退款接口
     */
    public String executeRefund(PayInfoVo payInfoVo) {
        return payStrategy.refund(payInfoVo);
    }


    /**
     * 根据策略对象，执行不同的关闭接口
     */
    public String executeCloseOrder(PayInfoVo payInfoVo) {
        return payStrategy.closeOrder(payInfoVo);
    }


    /**
     * 根据策略对象，执行不同的查询订单状态接口
     */
    public String executeQueryPayStatus(PayInfoVo payInfoVo) {
        return payStrategy.queryPayStatus(payInfoVo);
    }


}
