package com.example.dcloud_shop.component;

import com.example.dcloud_shop.entity.vo.PayInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 支付宝支付策略
 */

@Service
@Slf4j
public class AliPayStrategy implements PayStrategy {

    @Override
    public String unifiedOrder(PayInfoVo payInfoVo) {
        return "SUCCESS";
    }

    @Override
    public String refund(PayInfoVo payInfoVo) {
        return "SUCCESS";
    }

    @Override
    public String queryPayStatus(PayInfoVo payInfoVo) {
        return "SUCCESS";
    }

    @Override
    public String closeOrder(PayInfoVo payInfoVo) {
        return "SUCCESS";
    }
}
