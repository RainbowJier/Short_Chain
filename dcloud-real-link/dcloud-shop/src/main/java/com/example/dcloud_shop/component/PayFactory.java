package com.example.dcloud_shop.component;

import com.example.dcloud_common.enums.ProductOrderPayTypeEnum;
import com.example.dcloud_shop.entity.vo.PayInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 简单工厂模式，创建支付策略
 */
@Component
@Slf4j
public class PayFactory {

    @Autowired
    private AliPayStrategy aliPayStrategy;

    @Autowired
    private WechatPayStrategy wechatPayStrategy;

    /**
     * 创建支付，简单工厂模式
     */
    public String pay(PayInfoVo payInfoVo){
        // 支付类型
        String payType = payInfoVo.getPayType();

        //支付宝支付
        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)) {
            PayStrategyContext payStrategyContext = new PayStrategyContext(aliPayStrategy);
            return payStrategyContext.executeUnifiedOrder(payInfoVo);
        }
        //微信支付
        else if(ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeUnifiedOrder(payInfoVo);
        }
        return "";
    }


    /**
     * 关闭订单
     */
    public String closeOrder(PayInfoVo payInfoVo){
        String payType = payInfoVo.getPayType();

        //支付宝支付
        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)) {

            PayStrategyContext payStrategyContext = new PayStrategyContext(aliPayStrategy);
            return payStrategyContext.executeCloseOrder(payInfoVo);

        }
        //微信支付
        else if(ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeCloseOrder(payInfoVo);
        }
        return "";
    }


    /**
     * 查询支付状态
     */
    public String queryPayStatus(PayInfoVo payInfoVo){
        String payType = payInfoVo.getPayType();

        //支付宝支付
        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)) {
            PayStrategyContext payStrategyContext = new PayStrategyContext(aliPayStrategy);
            return payStrategyContext.executeQueryPayStatus(payInfoVo);

        }
        //微信支付
        else if(ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeQueryPayStatus(payInfoVo);
        }
        return "";
    }


    /**
     * 退款接口
     */
    public String refund(PayInfoVo payInfoVo){

        String payType = payInfoVo.getPayType();

        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)) {
            //支付宝支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(aliPayStrategy);
            return payStrategyContext.executeRefund(payInfoVo);

        } else if(ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            //微信支付
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeRefund(payInfoVo);
        }

        return "";
    }




}
