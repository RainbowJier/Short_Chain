package com.example.dcloud_link.listener;

import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.enums.EventMessageType;
import com.example.dcloud_common.exception.BizException;
import com.example.dcloud_link.service.ShortLinkService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎C 端队列监听器（消费者）
 * @Date: 2024/11/12 10:02
 * @Version: 1.0
 */
@Slf4j
@Component
@RabbitListener(queuesToDeclare = {@Queue("short_link.add.link.queue") }) // 如果没有队列，则自动创建队列
public class ShortLinkAddLinkMQListener {
    @Autowired
    private ShortLinkService shortLinkService;

    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        log.info("C 端监听到消息 ShortLinkAddLinkMQListener：message 消息内容：{}",message);

        try {
            eventMessage.setEventMessageType(EventMessageType.SHORT_LINK_ADD_LINK.name());

            // 处理消息
            shortLinkService.handlerAddShortLink(eventMessage);

        } catch (Exception e) {
            log.error("C 端消费异常：{}", e.getMessage());
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        log.info("C 端消费成功{}", eventMessage);
    }
}
