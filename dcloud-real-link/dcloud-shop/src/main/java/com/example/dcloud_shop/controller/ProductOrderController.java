package com.example.dcloud_shop.controller;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.ClientTypeEnum;
import com.example.dcloud_common.enums.ProductOrderPayTypeEnum;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_shop.controller.request.ConfirmOrderRequest;
import com.example.dcloud_shop.service.ProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.avatica.proto.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author RainbowJier
 * @since 2024-11-23
 */
@Controller
@RequestMapping("/api/order/v1")
@Slf4j
public class ProductOrderController {

    @Autowired
    private ProductOrderService productOrderService;

    /**
     * 分页查询订单列表
     */
    @GetMapping("/page")
    public JsonData page(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size,
                         @RequestParam(value = "state", required = false) String state){

        Map<String, Object> resultMap = productOrderService.page(page, size, state);
        return JsonData.buildSuccess(resultMap);
    }

    /**
     * 查询订单状态
     */
    @GetMapping("/query_state")
    public JsonData queryState(@RequestParam(value = "out_trade_no") String outTradeNo){
        String state = productOrderService.queryProductrOrderState(outTradeNo);

        return StringUtils.isEmpty(state)
                ? JsonData.buildResult(BizCodeEnum.ORDER_CONFIRM_NOT_EXIST)
                : JsonData.buildSuccess(state);
    }

    /**
     * 创建订单
     */
    @PostMapping("/confirm")
    public JsonData confirmOrder(@RequestBody ConfirmOrderRequest confirmOrderRequest, HttpServletResponse response) {
        // 创建订单
        JsonData jsonData = productOrderService.confirmOrder(confirmOrderRequest);

        if (jsonData.getCode() == 0) {
            String clientType = confirmOrderRequest.getClientType();  // 客户端类型
            String payType = confirmOrderRequest.getPayType();    // 支付方式

            // 支付宝支付，跳转网页
            if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.ALI_PAY.name())) {
                if (clientType.equalsIgnoreCase(ClientTypeEnum.APP.name())) {
                    CommonUtil.sendHtmlMessage(response, jsonData);
                } else if (clientType.equalsIgnoreCase(ClientTypeEnum.PC.name())) {

                } else if (clientType.equalsIgnoreCase(ClientTypeEnum.H5.name())) {

                }
            }
            // 微信支付，返回json
            else if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT_PAY.name())) {
                CommonUtil.sendJsonMessage(response, jsonData);
            }

        } else {
            log.error("创建确认失败：{}", jsonData);
            CommonUtil.sendJsonMessage(response, jsonData);
        }

        return JsonData.buildSuccess();
    }
}
