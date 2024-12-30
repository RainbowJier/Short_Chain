package com.example.dcloud_shop.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.example.dcloud_common.constant.RedisKey;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.ClientTypeEnum;
import com.example.dcloud_common.enums.ProductOrderPayTypeEnum;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_shop.annotation.RepeatSubmit;
import com.example.dcloud_shop.controller.request.ConfirmOrderRequest;
import com.example.dcloud_shop.controller.request.ProductOrderPageRequest;
import com.example.dcloud_shop.service.ProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequestMapping("/api/order/v1")
public class ProductOrderController {

    @Resource
    private ProductOrderService productOrderService;

    @Resource
    private StringRedisTemplate redisTemplate;

    /**
     * Generate order token to avoid repeating submission.
     */
    @GetMapping("/token")
    public JsonData getOrderToken() {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        String token = CommonUtil.getStringNumRandom(32);
        String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY, accountNo, token);

        redisTemplate.opsForValue().set(key, String.valueOf(Thread.currentThread().getId()), 30, TimeUnit.MINUTES);
        return JsonData.buildSuccess(token);
    }

    /**
     * Get order list by page.
     */
    @PostMapping("/page")
    public JsonData page(@RequestBody ProductOrderPageRequest orderPageRequest) {
        Map<String, Object> resultMap = productOrderService.page(orderPageRequest.getPage(), orderPageRequest.getSize(), orderPageRequest.getState());
        return JsonData.buildSuccess(resultMap);
    }

    /**
     * Get order status.
     */
    @GetMapping("/query_state")
    public JsonData queryState(@RequestParam(value = "out_trade_no") String outTradeNo) {
        String state = productOrderService.queryProductrOrderState(outTradeNo);

        return StringUtils.isEmpty(state)
                ? JsonData.buildResult(BizCodeEnum.ORDER_CONFIRM_NOT_EXIST)
                : JsonData.buildSuccess(state);
    }

    /**
     * Create order.
     */
    @PostMapping("/confirm")
    @RepeatSubmit(limitType = RepeatSubmit.Type.PARAM)
    public JsonData confirmOrder(@RequestBody ConfirmOrderRequest confirmOrderRequest, HttpServletResponse response) throws IOException {
        // 创建订单
        JsonData jsonData = productOrderService.confirmOrder(confirmOrderRequest);

        // 跳转支付页面
        //if (jsonData.getCode() == 0) {
        //    String clientType = confirmOrderRequest.getClientType();  // 客户端类型
        //    String payType = confirmOrderRequest.getPayType();    // 支付方式
        //
        //    // 支付宝支付，跳转网页
        //    if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.ALI_PAY.name())) {
        //        if (clientType.equalsIgnoreCase(ClientTypeEnum.APP.name())) {
        //            CommonUtil.sendHtmlMessage(response, jsonData);
        //        } else if (clientType.equalsIgnoreCase(ClientTypeEnum.PC.name())) {
        //            // todo: 跳转到支付宝支付页面
        //        } else if (clientType.equalsIgnoreCase(ClientTypeEnum.H5.name())) {
        //            // todo: 跳转到支付宝支付页面
        //        }
        //    }
        //    // 微信支付，返回json
        //    if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT_PAY.name())) {
        //        CommonUtil.sendJsonMessage(response, jsonData);
        //    }
        //
        //} else {
        //    log.error("创建确认失败：{}", jsonData);
        //    CommonUtil.sendJsonMessage(response, jsonData);
        //}
        return JsonData.buildSuccess();
    }
}
