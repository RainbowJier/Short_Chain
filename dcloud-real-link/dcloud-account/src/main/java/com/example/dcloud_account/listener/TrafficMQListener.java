package com.example.dcloud_account.listener;

import com.example.dcloud_account.service.TrafficService;
import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.exception.BizException;
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
        @Queue("order.traffic.queue"),        // 发放流量包队列
        @Queue("traffic.free_init.queue")   // 免费流量初始化队列
})
public class TrafficMQListener {
    @Autowired
    private TrafficService trafficService;

    @RabbitHandler
    public void orderHandler(EventMessage eventMessage, Message message, Channel channel){
        log.info("【发放流量包监听器】监听到消息，TrafficMQListener：message 消息内容：{}",message);

        try {
            // 发放流量包
            trafficService.handleTrafficMessage(eventMessage);

        } catch (Exception e) {
            log.error("【发放流量包监听器】消费异常：{}", e.getMessage());
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        log.info("【发放流量包监听器】消费成功{}", eventMessage);
    }
}
