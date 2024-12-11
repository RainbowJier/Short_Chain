
package com.example.dcloud_account.service.impl;


import com.example.dcloud_account.entity.Product;
import com.example.dcloud_account.entity.Traffic;
import com.example.dcloud_account.manager.TrafficManager;
import com.example.dcloud_account.service.TrafficService;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.EventMessageType;
import com.example.dcloud_common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class TrafficServiceImpl implements TrafficService {

    @Resource
    private TrafficManager trafficManager;

    /**
     * 流量包发放
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleTrafficMessage(EventMessage eventMessage) {
        String messageType = eventMessage.getEventMessageType();   // 订单支付类型

        // 订单已经支付，新增流量包
        if(messageType.equalsIgnoreCase(EventMessageType.PRODUCT_ORDER_PAY.name())){
            // 订单信息
            Long accountNo = eventMessage.getAccountNo();  // 账号
            String outTradeNo = eventMessage.getBizId();   // 订单号
            String content = eventMessage.getContent();
            Map<String, Object> orderInfoMap = JsonUtil.jsonStrToObj(content,Map.class);
            Integer buyNum = (Integer) orderInfoMap.get("buyNum");  // 购买数量
            String productSnapshotStr = (String) orderInfoMap.get("productSnapshot");  // 产品快照
            Product product = JsonUtil.jsonStrToObj(productSnapshotStr, Product.class);

            log.info("订单号：{}，购买数量：{}，产品快照：{}",outTradeNo,buyNum,product);

            // 流量包有效期
            LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(product.getValidDay());
            Date date = Date.from(expiredDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // 构建流量包对象
            Traffic traffic = Traffic.builder()
                    .accountNo(accountNo)
                    .dayLimit(product.getDayTimes() * buyNum)
                    .dayUsed(0)
                    .totalLimit(product.getTotalTimes())
                    .pluginType(product.getPluginType())
                    .level(product.getLevel())
                    .productId(product.getId())
                    .outTradeNo(outTradeNo)
                    .expiredDate(date)
                    .build();

            // 新增流量包
            int rows = trafficManager.add(traffic);
            log.info("【流量包消费者】新增流量包:rows={},traffic={}",rows,traffic);
        }
    }
}
