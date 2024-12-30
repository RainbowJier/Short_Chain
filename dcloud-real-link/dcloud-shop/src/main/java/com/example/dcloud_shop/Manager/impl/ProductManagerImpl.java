package com.example.dcloud_shop.Manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dcloud_shop.Manager.ProductManager;
import com.example.dcloud_shop.model.entity.Product;
import com.example.dcloud_shop.model.vo.ProductVo;
import com.example.dcloud_shop.mapper.ProductMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductManagerImpl implements ProductManager {
    @Resource
    private ProductMapper productMapper;

    @Override
    public List<ProductVo> list() {
        List<Product> list = productMapper.selectList(null);

        List<ProductVo> productVoList = new ArrayList<>();
        for (Product product : list) {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(product, productVo);
            productVoList.add(productVo);
        }

        return productVoList;
    }

    @Override
    public Product findDetailById(Long productId) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getId, productId);
        return productMapper.selectOne(queryWrapper);
    }
}
