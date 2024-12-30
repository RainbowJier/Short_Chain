package com.example.dcloud_shop.Manager.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_shop.Manager.ProductOrderManager;
import com.example.dcloud_shop.model.entity.ProductOrder;
import com.example.dcloud_shop.model.vo.ProductOrderVo;
import com.example.dcloud_shop.mapper.ProductOrderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


@Component
public class ProductOrderManagerImpl implements ProductOrderManager {

    @Resource
    private ProductOrderMapper productOrderMapper;

    @Override
    public int add(ProductOrder productOrder) {
        return productOrderMapper.insert(productOrder);
    }

    @Override
    public ProductOrder findByOutTradeNoAndAccountNo(String outTradeNo, Long accountNo) {
        LambdaQueryWrapper<ProductOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductOrder::getOutTradeNo, outTradeNo)
                .eq(ProductOrder::getAccountNo, accountNo)
                .eq(ProductOrder::getDel, 0);

        return productOrderMapper.selectOne(wrapper);
    }

    @Override
    public int updateOrderPayState(String outTradeNo, Long accountNo, String newState, String oldState) {
        LambdaUpdateWrapper<ProductOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProductOrder::getOutTradeNo, outTradeNo)
                .eq(ProductOrder::getAccountNo, accountNo)
                .eq(ProductOrder::getState, oldState)
                .set(ProductOrder::getState, newState);

        return productOrderMapper.update(null, wrapper);
    }

    @Override
    public Map<String, Object> page(int page, int size, Long accountNo, String state) {
        Page<ProductOrder> pageInfo = new Page<>(page, size);

        Page<ProductOrder> pageMap;

        // 订单状态为空时，查询所有订单
        if (StringUtils.isEmpty(state)) {
            LambdaQueryWrapper<ProductOrder> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductOrder::getAccountNo, accountNo)
                    .eq(ProductOrder::getDel, 0);
            pageMap = productOrderMapper.selectPage(pageInfo, wrapper);
        }
        // 订单状态不为空时，查询指定订单
        else {
            LambdaQueryWrapper<ProductOrder> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProductOrder::getAccountNo, accountNo)
                    .eq(ProductOrder::getState, state)
                    .eq(ProductOrder::getDel, 0);
            pageMap = productOrderMapper.selectPage(pageInfo, wrapper);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total_record", pageMap.getTotal()); // total number of records.
        result.put("total_page", pageMap.getPages());   // total number of pages.

        List<ProductOrderVo> list = new ArrayList<>();
        List<ProductOrder> records = pageMap.getRecords();  // current page data.
        for (ProductOrder object : records) {
            ProductOrderVo productOrderVo = new ProductOrderVo();
            BeanUtils.copyProperties(object, productOrderVo);
            list.add(productOrderVo);
        }
        result.put("current_data", list);

        return result;
    }

    @Override
    public int del(Long accountNo, String productOrderId) {
        LambdaUpdateWrapper<ProductOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProductOrder::getAccountNo, accountNo)
                .eq(ProductOrder::getId, productOrderId)
                .set(ProductOrder::getDel, 1);

        return productOrderMapper.update(null, wrapper);
    }


}
