package com.example.dcloud_shop.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/11/26 16:55
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrderPageRequest {
    private int page;

    private int size;

    /**
     * 订单状态
     */
    private String state;

}
