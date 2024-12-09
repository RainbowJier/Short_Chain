package com.example.dcloud_shop.component;

import com.example.dcloud_shop.entity.vo.PayInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class WechatPayStrategy implements PayStrategy {

    /**
     * 统一下单接口
     */
    @Override
    public String unifiedOrder(PayInfoVo payInfoVO) {
        return "SUCCESS";
    }

    /**
     * 退款接口
     */
    @Override
    public String refund(PayInfoVo payInfoVo) {
        return "SUCCESS";
    }

    /**
     * 查询支付状态
     */
    @Override
    public String queryPayStatus(PayInfoVo payInfoVo) {
        return "SUCCESS";
    }

    /**
     * 关闭订单
     */
    @Override
    public String closeOrder(PayInfoVo payInfoVo) {
        return "SUCCESS";
    }
}
