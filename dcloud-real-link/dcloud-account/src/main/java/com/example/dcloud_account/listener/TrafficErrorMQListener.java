package com.example.dcloud_account.listener;

import com.example.dcloud_common.entity.EventMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎订单消费失败，异常处理队列
 * @Date: 2024/11/12 10:02
 * @Version: 1.0
 */
@Slf4j
@Component
@RabbitListener(queuesToDeclare = {@Queue("order.error.queue")})
public class TrafficErrorMQListener {

    @RabbitHandler
    public void orderErrorMQHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        log.info("警告：订单消费事件失败，异常处理队列，eventMessage内容:{}", eventMessage);
        log.info("警告：Message:{}", message);
        log.info("警告成功，发送通知短信");
    }
}
