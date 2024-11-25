package com.example.dcloud_shop.service;

import com.example.dcloud_shop.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dcloud_shop.entity.vo.ProductVo;

import java.util.List;

/**
 * <p>
 * 产品表 服务类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
public interface ProductService {

    /**
     * 获取商品列表
     */
    List<ProductVo> list();


    /**
     * 根据id获取商品详情
     */
    ProductVo findDetailById(Long productId);
}
