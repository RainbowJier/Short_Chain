package com.example.dcloud_shop.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/11/12 17:27
 * @Version: 1.0
 */

@Data
@Configuration
public class RabbitMQErrorConfig {
    /**
     * 交换机名称
     */
    private String orderErrorExchange = "order.error.exchange";
    /**
     * 队列名称
     */
    private String orderErrorQueue = "order.error.queue";
    /**
     * 路由键
     */
    private String orderErrorBindKey = "order.error.routing.key";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 异常交换机
     */
    @Bean
    public Exchange errorTopicExchange() {
        return new TopicExchange(orderErrorExchange, true, false);
    }

    /**
     * 创建异常队列
     */
    @Bean
    public Queue errorQueue() {
        return new Queue(orderErrorQueue, true);
    }

    /**
     * 队列与交换机进行绑定
     */
    @Bean
    public Binding bindingErrorQueueAndExchange(Queue errorQueue, TopicExchange errorTopicExchange) {
        return BindingBuilder
                .bind(errorQueue())
                .to(errorTopicExchange())
                .with(orderErrorBindKey)
                .noargs();
    }


    /**
     * 消息重转发器
     */
    @Bean
    public MessageRecoverer messageRecoverer() {
        return new RepublishMessageRecoverer(rabbitTemplate, orderErrorExchange, orderErrorBindKey);
    }
}
