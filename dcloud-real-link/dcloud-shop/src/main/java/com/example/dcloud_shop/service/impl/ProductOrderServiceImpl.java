package com.example.dcloud_shop.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.example.dcloud_common.constant.TimeConstant;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.entity.LoginUser;
import com.example.dcloud_common.enums.*;
import com.example.dcloud_common.exception.BizException;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.IDUtil;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_common.util.JsonUtil;
import com.example.dcloud_shop.Manager.ProductManager;
import com.example.dcloud_shop.Manager.ProductOrderManager;
import com.example.dcloud_shop.component.PayFactory;
import com.example.dcloud_shop.config.RabbitMQConfig;
import com.example.dcloud_shop.controller.request.ConfirmOrderRequest;
import com.example.dcloud_shop.entity.Product;
import com.example.dcloud_shop.entity.ProductOrder;
import com.example.dcloud_shop.entity.vo.PayInfoVo;
import com.example.dcloud_shop.service.ProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RainbowJier
 * @since 2024-11-23
 */

@Slf4j
@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Resource
    private ProductOrderManager productOrderManager;

    @Resource
    private ProductManager productManager;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitMQConfig rabbitMQConfig;

    @Resource
    private PayFactory payFactory;

    /**
     * 分页查询
     */
    @Override
    public Map<String, Object> page(int page, int size, String state) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        return productOrderManager.page(page, size, accountNo, state);
    }

    @Override
    public String queryProductrOrderState(String outTradeNo) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        ProductOrder productOrder = productOrderManager.findByOutTradeNoAndAccountNo(outTradeNo, accountNo);
        if (productOrder == null) {
            return "";
        } else {
            return productOrder.getState();
        }
    }

    /**
     * 创建订单
     * 1. 防止重复提交（重点）
     * 2. 获取最新的流量包价格
     * 3. 订单验价
     * a. 如果有优惠券或者其他折扣
     * b. 验证前端显示的价格和后台计算的价格是否相同
     * 4. 发送订单对象，保存数据库
     * 5. 发送延迟消息，订单超时，自动关闭订单（重点）
     * 6. todo:创建支付信息，对接第三方支付（重点）
     * 7. todo:回调，更新订单支付状态（重点）
     * 8. todo:支付成功，创建流量包（重点）
     */
    @Override
    @Transactional
    public JsonData confirmOrder(ConfirmOrderRequest orderRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        String orderOutTradeNo = CommonUtil.getStringNumRandom(32);

        // 获取最新的商品
        Product product = productManager.findDetailById(orderRequest.getProductId());

        // 验证价格
        this.checkPrice(product, orderRequest);

        // 保存订单到数据库
        ProductOrder productOrder = this.saveProductOrder(orderRequest, loginUser, orderOutTradeNo, product);

        // 创建支付信息，对接第三方支付
        PayInfoVo payInfoVo = new PayInfoVo()
                .setAccountNo(accountNo)
                .setClientType(orderRequest.getClientType())
                .setPayType(orderRequest.getPayType())
                .setTitle(product.getTitle())
                .setDescription("")
                .setPayFee(orderRequest.getPayAmount())
                .setOrderPayTimeoutMills(TimeConstant.ORDER_PAY_TIMEOUT_MILLS);

        // 发送延迟消息，订单超时，自动关闭订单
        EventMessage eventMessage = EventMessage.builder()
                .messageId(IDUtil.generateSnowFlakeID().toString())
                .eventMessageType(EventMessageType.PRODUCT_ORDER_NEW.name())
                .accountNo(accountNo)
                .bizId(orderOutTradeNo)
                .build();

        rabbitTemplate.convertAndSend(
                rabbitMQConfig.getOrderEventExchange(),
                rabbitMQConfig.getOrderCloseDelayRoutingKey(),
                eventMessage);

        // 调用支付信息
        String codeUrl = payFactory.pay(payInfoVo);   // 返回的是二维码，这里默认创建成功
        if(StringUtils.isNotBlank(codeUrl)){
            Map<String,String> resultMap = new HashMap<>(2);
            resultMap.put("code_url",codeUrl);   // 二维码
            resultMap.put("out_trade_no",payInfoVo.getOutTradeNo());
            return JsonData.buildSuccess(resultMap);
        }

        return JsonData.buildResult(BizCodeEnum.PAY_ORDER_FAIL);
    }

    /**
     * 保存订单
     */
    private ProductOrder saveProductOrder(ConfirmOrderRequest orderRequest, LoginUser loginUser, String orderOutTradeNo, Product product) {
        // 设置用户基本信息
        ProductOrder productOrder = new ProductOrder()
                .setAccountNo(loginUser.getAccountNo())
                .setNickname(loginUser.getUsername());

        // 设置商品信息
        productOrder.setProductId(orderRequest.getProductId())
                .setProductTitle(product.getTitle())
                .setProductSnapshot(JsonUtil.objToJsonStr(product))
                .setProductAmount(product.getAmount());

        // 设置订单信息
        productOrder.setBuyNum(orderRequest.getBuyNum())
                .setOutTradeNo(orderOutTradeNo)
                .setCreateTime(new Date())
                .setDel(0);

        // 设置发票信息
        productOrder.setBillType(BillTypeEnum.valueOf(orderRequest.getBillType()).name())
                .setBillHeader(orderRequest.getBillHeader())
                .setBillReceiverPhone(orderRequest.getBillReceiverPhone())
                .setBillReceiverEmail(orderRequest.getBillReceiverEmail())
                .setBillContent(orderRequest.getBillContent());

        // 设置订单总价
        productOrder.setTotalAmount(orderRequest.getTotalAmount()) // 订单总价
                .setPayAmount(orderRequest.getTotalAmount())  // 支付金额
                .setState(ProductOrderStateEnum.NEW.name())   // 订单状态
                .setPayType(ProductOrderPayTypeEnum.valueOf(orderRequest.getPayType()).name());  // 支付类型

        // 保存订单
        productOrderManager.add(productOrder);

        return productOrder;
    }

    /**
     * 校验前端/后端价格计算是否一致
     */
    private void checkPrice(Product product, ConfirmOrderRequest orderRequest) {
        // 如果有优惠券也要计算

        // 计算后台价格
        int buyNum = orderRequest.getBuyNum();  // 购买数量
        BigDecimal amount = product.getAmount(); // 商品最新价格
        BigDecimal totalPrice = BigDecimal.valueOf(buyNum).multiply(amount);   // 总价格

        // 前端计算价格
        BigDecimal totalAmount = orderRequest.getTotalAmount();

        if (totalPrice.compareTo(totalAmount) != 0) {
            log.error("验证价格失败，前端计算价格: {} 和后台计算价格:{} 不一致", totalPrice, totalAmount);
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_PRICE_FAIL);
        }
    }


    /**
     * 关闭订单
     */
    @Override
    public boolean closeProductOrder(EventMessage eventMessage) {
        String outTradeNo = eventMessage.getBizId();  // 订单号
        Long accountNo = eventMessage.getAccountNo();  // 用户账号

        ProductOrder productOrder = productOrderManager.findByOutTradeNoAndAccountNo(outTradeNo, accountNo);

        // 订单不存在
        if(productOrder == null){
            log.warn("订单不存在，订单号: {}", outTradeNo);
            return true;
        }

        String state = productOrder.getState();
        // 已经支付
        if(state.equalsIgnoreCase(ProductOrderStateEnum.PAY.name())){
            log.info("订单已经支付，订单号: {}",eventMessage);
        }

        String payResult = "";  // 支付状态

        // 还未支付，查询第三方支付接口再次校验支付状态
        if(state.equalsIgnoreCase(ProductOrderStateEnum.NEW.name())){
            PayInfoVo payInfoVo = new PayInfoVo()
                    .setPayType(productOrder.getPayType())
                    .setOutTradeNo(outTradeNo)
                    .setAccountNo(accountNo);

            // 查询第三方支付接口，校验支付状态，默认成功
            payResult = "SUCCESS";

            if(StringUtils.isBlank(payResult)){
                // 支付失败，订单关闭
                productOrderManager.updateOrderPayState(outTradeNo,accountNo,ProductOrderStateEnum.CONCEL.name(),ProductOrderStateEnum.NEW.name());
                log.info("订单未支付，取消订单: {}",eventMessage);
            }else{
                log.warn("订单已支付，但是微信回调通知失败，需要排查: {}",eventMessage);
                // 支付成功，订单支付成功
                productOrderManager.updateOrderPayState(outTradeNo,accountNo,ProductOrderStateEnum.PAY.name(),ProductOrderStateEnum.NEW.name());
            }
        }

        // todo:触发支付成功后的逻辑，创建流量包等

        return true;
    }


    /**
     * 查询第三方支付接口，校验支付状态
     */
    private boolean checkPayResult(PayInfoVo payInfoVo){


        return true;
    }
}