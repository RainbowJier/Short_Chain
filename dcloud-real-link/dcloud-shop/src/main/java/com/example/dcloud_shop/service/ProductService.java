package com.example.dcloud_shop.service;

import com.example.dcloud_shop.model.vo.ProductVo;

import java.util.List;

public interface ProductService {

    /**
     * Get all products
     */
    List<ProductVo> list();


    /**
     * Get product detail by id
     */
    ProductVo findDetailById(Long productId);
}
