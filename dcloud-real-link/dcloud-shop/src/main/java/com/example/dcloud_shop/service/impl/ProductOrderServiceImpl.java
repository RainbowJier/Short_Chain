package com.example.dcloud_shop.service.impl;

import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_shop.Manager.ProductOrderManager;
import com.example.dcloud_shop.controller.request.ConfirmOrderRequest;
import com.example.dcloud_shop.entity.ProductOrder;
import com.example.dcloud_shop.mapper.ProductOrderMapper;
import com.example.dcloud_shop.service.ProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Resource
    private ProductOrderManager productOrderManager;

    /**
     * 分页查询
     */
    @Override
    public Map<String, Object> page(int page, int size, String state) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        return productOrderManager.page(page, size, accountNo,state);
    }

    @Override
    public String queryProductrOrderState(String outTradeNo) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        ProductOrder productOrder = productOrderManager.findByOutTradeNoAndAccountNo(outTradeNo,accountNo);
        if(productOrder == null){
            return "";
        }else{
            return productOrder.getState();
        }
    }

    /**
     * 创建订单
     */
    @Override
    public JsonData confirmOrder(ConfirmOrderRequest confirmOrderRequest) {
        return null;
    }


}
