package com.example.dcloud_shop.Manager;

import com.example.dcloud_shop.entity.ProductOrder;

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

    /**
     * 新增订单
     */
    int add(ProductOrder productOrder);

    /**
     * 通过订单号和账号查询
     */
    ProductOrder findByOutTradeNoAndAccountNo(String outTradeNo, String accountNo);


    /**
     * 更新订单支付状态
     */
    int updateOrderPayState(String outTradeNo, Long accountNo, String newState, String oldState);

    /**
     * 分页查询订单
     */
    Map<String, Object> page(int page, int size, Long accountNo, String state);


}
