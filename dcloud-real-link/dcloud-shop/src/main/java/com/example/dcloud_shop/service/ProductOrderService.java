package com.example.dcloud_shop.service;

import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.ProductOrderPayTypeEnum;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_shop.controller.request.ConfirmOrderRequest;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
public interface ProductOrderService {

    /**
     * 分页查询
     */
    Map<String, Object> page(int page, int size, String state);

    /**
     * 查询订单状态
     */
    String queryProductrOrderState(String outTradeNo);

    /**
     * 创建订单
     */
    JsonData confirmOrder(ConfirmOrderRequest confirmOrderRequest);

    /**
     * 关闭订单
     */
    boolean closeProductOrder(EventMessage eventMessage);

    /**
     * 支付成功后，更新订单、发放流量包
     */
    void handleProductOrderMessage(EventMessage eventMessage);
}
