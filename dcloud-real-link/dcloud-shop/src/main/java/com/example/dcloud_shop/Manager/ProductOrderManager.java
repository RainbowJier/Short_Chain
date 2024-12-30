package com.example.dcloud_shop.Manager;

import com.example.dcloud_shop.model.entity.ProductOrder;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
public interface ProductOrderManager {

    int add(ProductOrder productOrder);

    ProductOrder findByOutTradeNoAndAccountNo(String outTradeNo, Long accountNo);

    /**
     * Update order payment status.
     */
    int updateOrderPayState(String outTradeNo, Long accountNo, String newState, String oldState);

    /**
     * Get order list by page.
     */
    Map<String, Object> page(int page, int size, Long accountNo, String state);


    /**
     * Delete order.
     */
    int del(Long accountNo,String productOrderId);
}
