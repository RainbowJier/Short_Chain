package com.example.dcloud_shop.service.impl;

import com.example.dcloud_common.constant.TimeConstant;
import com.example.dcloud_common.entity.LoginUser;
import com.example.dcloud_common.enums.BillTypeEnum;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.ProductOrderPayTypeEnum;
import com.example.dcloud_common.enums.ProductOrderStateEnum;
import com.example.dcloud_common.exception.BizException;
import com.example.dcloud_common.interceptor.LoginInterceptor;
import com.example.dcloud_common.util.CommonUtil;
import com.example.dcloud_common.util.JsonData;
import com.example.dcloud_common.util.JsonUtil;
import com.example.dcloud_shop.Manager.ProductManager;
import com.example.dcloud_shop.Manager.ProductOrderManager;
import com.example.dcloud_shop.controller.request.ConfirmOrderRequest;
import com.example.dcloud_shop.entity.Product;
import com.example.dcloud_shop.entity.ProductOrder;
import com.example.dcloud_shop.entity.vo.PayInfoVo;
import com.example.dcloud_shop.service.ProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
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
     * 1. todo:防止重复提交（重点）
     * 2. 获取最新的流量包价格
     * 3. 订单验价
     * a. 如果有优惠券或者其他折扣
     * b. 验证前端显示的价格和后台计算的价格是否相同
     * 4. 发送订单对象，保存数据库
     * 5. todo:发送延迟消息，订单超时，自动关闭订单（重点）
     * 6. todo:创建支付信息，对接第三方支付（重点）
     * 7. todo:回调，更新订单支付状态（重点）
     * 8. todo:支付成功，创建流量包（重点）
     */
    @Override
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

        // todo:更新支付状态

        // todo:支付成功，创建流量包


        return null;
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

}
