package com.example.dcloud_shop.listener;

import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.EventMessageType;
import com.example.dcloud_common.exception.BizException;
import com.example.dcloud_shop.service.ProductOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎C 端队列监听器（消费者）
 * @Date: 2024/11/12 10:02
 * @Version: 1.0
 */
@Slf4j
@Component
@RabbitListener(queuesToDeclare = {
        @Queue("order.close.queue"),
        @Queue("order.update.queue"),
        @Queue("order.traffic.queue"),
}) // 如果没有队列，则自动创建队列

public class ProductOrderMQListener {
    @Autowired
    private ProductOrderService productOrderService;

    @RabbitHandler
    public void orderHandler(EventMessage eventMessage, Message message, Channel channel){
        log.info("监听到消息 ProductOrderMQListener：message 消息内容：{}",message);

        try {
            // 支付成功后，更新订单状态，关闭订单
            productOrderService.handleProductOrderMessage(eventMessage);

        } catch (Exception e) {
            log.error("消费异常：{}", e.getMessage());
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        log.info("消费成功{}", eventMessage);
    }
}
