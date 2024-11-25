package com.example.dcloud_shop.Manager.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dcloud_shop.Manager.ProductOrderManager;
import com.example.dcloud_shop.entity.ProductOrder;
import com.example.dcloud_shop.entity.vo.ProductOrderVo;
import com.example.dcloud_shop.mapper.ProductOrderMapper;
import com.example.dcloud_shop.service.ProductOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
@Component
public class ProductOrderManagerImpl implements ProductOrderManager {

    @Resource
    private ProductOrderMapper productOrderMapper;

    /**
     * 新增订单
     */
    @Override
    public int add(ProductOrder productOrder) {
        return productOrderMapper.insert(productOrder);
    }

    /**
     * 通过订单号和账号查询
     */
    @Override
    public ProductOrder findByOutTradeNoAndAccountNo(String outTradeNo, Long accountNo) {
        LambdaQueryWrapper<ProductOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductOrder::getOutTradeNo, outTradeNo)
                .eq(ProductOrder::getAccountNo, accountNo)
                .eq(ProductOrder::getDel, 0);

        return productOrderMapper.selectOne(wrapper);
    }

    /**
     * 更新订单支付状态
     */
    @Override
    public int updateOrderPayState(String outTradeNo, Long accountNo, String newState, String oldState) {
        LambdaUpdateWrapper<ProductOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProductOrder::getOutTradeNo, outTradeNo)
                .eq(ProductOrder::getAccountNo, accountNo)
                .eq(ProductOrder::getState, oldState)
                .set(ProductOrder::getState, newState);

        return productOrderMapper.update(null, wrapper);
    }

    /**
     * 分页查询订单
     */
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
        result.put("total_record", pageMap.getTotal()); // 总数据量
        result.put("total_page", pageMap.getPages());   // 总页数

        List<ProductOrderVo> list = new ArrayList<>();
        List<ProductOrder> records = pageMap.getRecords();  // 获取数据列表
        for (ProductOrder object : records) {
            ProductOrderVo productOrderVo = new ProductOrderVo();
            BeanUtils.copyProperties(object, productOrderVo);
            list.add(productOrderVo);
        }
        result.put("current_data", list); // 当前页的数据量

        return result;
    }

    /**
     * 删除订单
     */
    @Override
    public int del(Long accountNo, String productOrderId) {
        LambdaUpdateWrapper<ProductOrder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProductOrder::getAccountNo, accountNo)
                .eq(ProductOrder::getId, productOrderId)
                .set(ProductOrder::getDel, 1);

        return productOrderMapper.update(null, wrapper);
    }


}
