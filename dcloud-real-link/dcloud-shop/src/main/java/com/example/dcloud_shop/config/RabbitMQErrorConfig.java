package com.example.dcloud_shop.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RabbitMQErrorConfig {

    private String orderErrorExchange = "order.error.exchange";

    private String orderErrorQueue = "order.error.queue";

    private String orderErrorRoutingKey = "order.error.routing.key";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public TopicExchange errorTopicExchange() {
        return new TopicExchange(orderErrorExchange, true, false);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(orderErrorQueue, true);
    }

    @Bean
    public Binding bindingErrorQueueAndExchange(){
        return BindingBuilder
                .bind(errorQueue())
                .to(errorTopicExchange())
                .with(orderErrorRoutingKey);
    }

    /**
     * 消息重转发器
     * 消费消息重试⼀定次数后，⽤特定的routingKey转发到指定的交换机中，⽅便后续排查和告警
     */
    @Bean
    public MessageRecoverer messageRecoverer() {
        return new RepublishMessageRecoverer(rabbitTemplate, orderErrorExchange, orderErrorRoutingKey);
    }
}
