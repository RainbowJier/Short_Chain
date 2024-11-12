package com.example.dcloud_link.listener;

import com.example.dcloud_common.entity.EventMessage;
import com.example.dcloud_common.enums.BizCodeEnum;
import com.example.dcloud_common.exception.BizException;
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
 * @Description: 👺🐉😎B 端队列监听器（消费者）
 * @Date: 2024/11/12 10:02
 * @Version: 1.0
 */
@Slf4j
@Component
//@RabbitListener(queues = "short_link.add.mapping.queue") // 如果没有匹配到队列，则报错
@RabbitListener(queuesToDeclare = {@Queue("short_link.add.mapping.queue") }) // 如果没有队列，则自动创建队列
public class ShortLinkAddMappingMQListener {

    @RabbitHandler
    public void shortLinkHandler(EventMessage eventMessage, Message message, Channel channel) throws IOException {
        log.info("监听到消息 ShortLinkAddMappingMQListener：message 消息内容：{}",message);

        // 获取消息id
        long msgTag = message.getMessageProperties().getDeliveryTag();

        try {
            //TODO 处理业务
            int i = 1/0;  // 触发异常，测试消费失败

        } catch (Exception e) {
            log.error("消费失败{}", eventMessage);
            throw new BizException(BizCodeEnum.MQ_CONSUME_EXCEPTION);
        }
        log.info("消费成功{}", eventMessage);

        // 确认消息消费成功
        //channel.basicAck(msgTag, false);
    }
}
