package com.example.dcloud_shop.service.impl;

import com.example.dcloud_shop.Manager.ProductManager;
import com.example.dcloud_shop.entity.Product;
import com.example.dcloud_shop.entity.vo.ProductVo;
import com.example.dcloud_shop.mapper.ProductMapper;
import com.example.dcloud_shop.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 产品表 服务实现类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductManager productManager;


    @Override
    public List<ProductVo> list() {
        return productManager.list();
    }

    @Override
    public ProductVo findDetailById(Long productId) {
        Product product = productManager.findDetailById(productId);

        ProductVo productVo = new ProductVo();
        BeanUtils.copyProperties(product, productVo);

        return  productVo;
    }
}
