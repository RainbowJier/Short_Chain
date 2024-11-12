package com.example.dcloud_link.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
    private String shortLinkErrorExchange = "short_link.error.exchange";
    /**
     * 队列名称
     */
    private String shortLinkErrorQueue = "short_link.error.queue";
    /**
     * 路由键
     */
    private String shortLinkErrorRoutingKey = "short_link.error.routing.key";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 异常交换机
     */
    @Bean
    public TopicExchange errorTopicExchange(){
        return new TopicExchange(shortLinkErrorExchange,true,false);
    }

    /**
     * 创建异常队列
     */
    @Bean
    public Queue errorQueue(){
        return new Queue(shortLinkErrorQueue,true);
    }

    /**
     * 队列与交换机进行绑定
     */
    @Bean
    public Binding bindingErrorQueueAndExchange(Queue errorQueue, TopicExchange errorTopicExchange){
        return BindingBuilder
                .bind(errorQueue)
                .to(errorTopicExchange)
                .with(shortLinkErrorRoutingKey);
    }

    /**
     * 配置 RepublishMessageRecoverer
     * 用途：消息重试⼀定次数后，⽤特定的routingKey转发到指定的交换机中，⽅便后续排查和告警
     * 顶层是 MessageRecoverer 接⼝，多个实现类
     */
    @Bean
    public MessageRecoverer messageRecoverer(){
        return new RepublishMessageRecoverer(rabbitTemplate,shortLinkErrorExchange,shortLinkErrorRoutingKey);
    }
}
