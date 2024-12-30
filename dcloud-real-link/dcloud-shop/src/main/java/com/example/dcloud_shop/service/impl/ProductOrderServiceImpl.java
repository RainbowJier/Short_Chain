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
import com.example.dcloud_shop.model.entity.Product;
import com.example.dcloud_shop.model.entity.ProductOrder;
import com.example.dcloud_shop.model.vo.PayInfoVo;
import com.example.dcloud_shop.service.ProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonData confirmOrder(ConfirmOrderRequest orderRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        String orderOutTradeNo = CommonUtil.getStringNumRandom(32);

        Product product = productManager.findDetailById(orderRequest.getProductId());

        checkPrice(product, orderRequest);

        saveProductOrder(orderRequest, loginUser, orderOutTradeNo, product);

        // Send message that closes order automatically to Delay queue.
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

        // Payment information.
        PayInfoVo payInfoVo = new PayInfoVo()
                .setAccountNo(accountNo)
                .setClientType(orderRequest.getClientType())
                .setPayType(orderRequest.getPayType())
                .setTitle(product.getTitle())
                .setDescription("")
                .setPayFee(orderRequest.getPayAmount())
                .setOrderPayTimeoutMills(TimeConstant.ORDER_PAY_TIMEOUT_MILLS);

        String codeUrl = payFactory.pay(payInfoVo);

        // return QR code.
        //if (StringUtils.isNotBlank(codeUrl)) {
        //    Map<String, String> resultMap = new HashMap<>(2);
        //    resultMap.put("code_url", codeUrl);   // 二维码
        //    resultMap.put("out_trade_no", orderOutTradeNo);  // 订单号
        //    return JsonData.buildSuccess(resultMap);
        //}

        HashMap<String, Object> payMap = new HashMap<>(2);
        payMap.put("out_trade_no", orderOutTradeNo);
        payMap.put("account_no", accountNo);
        payMap.put("trade_state", "SUCCESS");
        boolean payState = processOrderCallbackMsg(ProductOrderPayTypeEnum.WECHAT_PAY, payMap);

        if (payState) {
            return JsonData.buildSuccess("Pay success");
        } else {
            JsonData.buildResult(BizCodeEnum.PAY_ORDER_CALLBACK_NOT_SUCCESS);
        }

        return JsonData.buildResult(BizCodeEnum.PAY_ORDER_FAIL);
    }

    private void checkPrice(Product product, ConfirmOrderRequest orderRequest) {
        BigDecimal totalFrontAmount = orderRequest.getTotalAmount();

        int buyNum = orderRequest.getBuyNum();
        BigDecimal amount = product.getAmount();
        BigDecimal totalEndAmount = BigDecimal.valueOf(buyNum).multiply(amount);

        if (totalEndAmount.compareTo(totalFrontAmount) != 0) {
            log.error("Failed to check the price, Front price = {}, Back price = {}", totalFrontAmount, totalEndAmount);
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_PRICE_FAIL);
        }
    }

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

    public boolean processOrderCallbackMsg(ProductOrderPayTypeEnum payType, Map<String, Object> paramsMap) {
        Long accountNo = Long.valueOf(paramsMap.get("account_no").toString());
        String outTradeNo = paramsMap.get("out_trade_no").toString();
        String tradeState = paramsMap.get("trade_state").toString();

        ProductOrder productOrder = productOrderManager.findByOutTradeNoAndAccountNo(outTradeNo, accountNo);

        Map<String, Object> content = new HashMap<>(4);
        content.put("outTradeNo", outTradeNo);
        content.put("buyNum", productOrder.getBuyNum());
        content.put("accountNo", accountNo);
        content.put("productSnapshot", productOrder.getProductSnapshot());

        EventMessage eventMessage = EventMessage.builder()
                .bizId(outTradeNo)
                .accountNo(accountNo)
                .messageId(outTradeNo)
                .content(JsonUtil.objToJsonStr(content))
                .eventMessageType(EventMessageType.PRODUCT_ORDER_PAY.name())
                .build();

        if ("SUCCESS".equals(tradeState)) {
            if (payType.name().equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT_PAY.name())) {
                Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(outTradeNo, "OK", 3, TimeUnit.DAYS);

                // Release traffic and update order status.
                if (Boolean.TRUE.equals(flag)) {
                    rabbitTemplate.convertAndSend(
                            rabbitMQConfig.getOrderEventExchange(),
                            rabbitMQConfig.getOrderUpdateTrafficRoutingKey(),
                            eventMessage);

                    return true;
                }
            }

            if (payType.name().equalsIgnoreCase(ProductOrderPayTypeEnum.ALI_PAY.name())) {
                // 支付宝支付回调逻辑
                return true;
            }
        }

        return false;
    }


    // =============== RabbitMQ Handler=====================
    @Override
    public void handleProductOrderMessage(EventMessage eventMessage) {
        String messageType = eventMessage.getEventMessageType();
        try {
            // Automatically close the order if payment is not made after a certain period of time.
            if (messageType.equalsIgnoreCase(EventMessageType.PRODUCT_ORDER_NEW.name())) {
                this.closeProductOrder(eventMessage);
            }
            // Pay success and update order status.
            else if (messageType.equalsIgnoreCase(EventMessageType.PRODUCT_ORDER_PAY.name())) {
                String outTradeNo = eventMessage.getBizId();
                Long accountNo = eventMessage.getAccountNo();
                String content = eventMessage.getContent();

                int rows = productOrderManager.updateOrderPayState(outTradeNo, accountNo, ProductOrderStateEnum.PAY.name(), ProductOrderStateEnum.NEW.name());
                log.info("订单支付成功，rows: {}, everMessage: {}", rows, eventMessage);
            }
        } catch (Exception e) {
            log.error("订单消费者失败, 错误信息: {}", e.getMessage());
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
    }

    public void closeProductOrder(EventMessage eventMessage) {
        String outTradeNo = eventMessage.getBizId();
        Long accountNo = eventMessage.getAccountNo();

        ProductOrder productOrder = productOrderManager.findByOutTradeNoAndAccountNo(outTradeNo, accountNo);

        if (productOrder == null) {
            log.warn("This order dose not exist，outTradeNo =  {}", outTradeNo);
            return;
        }

        String state = productOrder.getState();
        if (state.equalsIgnoreCase(ProductOrderStateEnum.PAY.name())) {
            log.info("Payment is made, outTradeNo = {}", outTradeNo);
        }

        String payStatus;
        // 还未支付，查询第三方支付接口再次校验支付状态
        if (state.equalsIgnoreCase(ProductOrderStateEnum.NEW.name())) {
            PayInfoVo payInfoVo = new PayInfoVo()
                    .setPayType(productOrder.getPayType())
                    .setOutTradeNo(outTradeNo)
                    .setAccountNo(accountNo);

            // 查询第三方支付接口，校验支付状态，默认成功
            payStatus = "SUCCESS";

            if (StringUtils.isBlank(payStatus)) {
                // 支付失败，订单关闭
                productOrderManager.updateOrderPayState(outTradeNo, accountNo, ProductOrderStateEnum.CANCEL.name(), ProductOrderStateEnum.NEW.name());
                log.info("订单未支付，取消订单: {}", eventMessage);
            } else {
                log.warn("订单已支付，但是微信回调通知失败，需要排查: {}", eventMessage);
                // 支付成功，订单支付成功
                productOrderManager.updateOrderPayState(outTradeNo, accountNo, ProductOrderStateEnum.PAY.name(), ProductOrderStateEnum.NEW.name());
            }
        }
    }


}