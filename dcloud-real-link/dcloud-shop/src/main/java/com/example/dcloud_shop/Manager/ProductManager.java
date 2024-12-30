package com.example.dcloud_shop.Manager;

import com.example.dcloud_shop.model.entity.Product;
import com.example.dcloud_shop.model.vo.ProductVo;

import java.util.List;

/**
 * <p>
 * 产品表 服务类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
public interface ProductManager {


    List<ProductVo> list();

    Product findDetailById(Long productId);
}
