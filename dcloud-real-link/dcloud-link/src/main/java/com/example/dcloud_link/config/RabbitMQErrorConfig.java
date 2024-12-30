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

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * Exchange
     */
    private String shortLinkErrorExchange = "short_link.error.exchange";

    @Bean
    public TopicExchange errorTopicExchange(){
        return new TopicExchange(shortLinkErrorExchange,true,false);
    }

    /**
     * Queue
     */
    private String shortLinkErrorQueue = "short_link.error.queue";

    private String shortLinkErrorBindKey = "short_link.error.routing.key";

    @Bean
    public Queue errorQueue(){
        return new Queue(shortLinkErrorQueue,true);
    }

    @Bean
    public Binding bindingErrorQueueAndExchange(Queue errorQueue, TopicExchange errorTopicExchange){
        return BindingBuilder
                .bind(errorQueue)
                .to(errorTopicExchange)
                .with(shortLinkErrorBindKey);
    }

    /**
     * The message will be sent to exception exchange if it fails to be routed to the queue.
     */
    @Bean
    public MessageRecoverer messageRecoverer(){
        return new RepublishMessageRecoverer(rabbitTemplate,shortLinkErrorExchange,shortLinkErrorBindKey);
    }
}
