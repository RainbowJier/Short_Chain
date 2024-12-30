package com.example.dcloud_shop.service;

import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.ProductOrderPayTypeEnum;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_shop.controller.request.ConfirmOrderRequest;

import java.util.Map;


public interface ProductOrderService {

    /**
     * Get order list by page.
     */
    Map<String, Object> page(int page, int size, String state);

    /**
     * Get order status.
     */
    String queryProductrOrderState(String outTradeNo);

    /**
     * Create product order.
     */
    JsonData confirmOrder(ConfirmOrderRequest confirmOrderRequest);

    void handleProductOrderMessage(EventMessage eventMessage);
}
