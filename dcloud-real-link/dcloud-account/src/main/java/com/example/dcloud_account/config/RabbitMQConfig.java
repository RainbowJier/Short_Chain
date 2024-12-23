package com.example.dcloud_account.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎延迟队列配置类
 * @Date: 2024/11/12 9:40
 * @Version: 1.0
 */


@Data
@Configuration
public class RabbitMQConfig {
    /**
     * message converter，message serializer
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * exchange.
     */
    private String trafficEventExchange = "traffic.event.exchange";

    @Bean
    public TopicExchange trafficEventExchange(){
        return new TopicExchange(trafficEventExchange,true,false);
    }

    //===============free traffic init queue==================================
    private String trafficFreeInitRoutingKey = "traffic.free_init.routing.key";

    private String trafficFreeInitQueue = "traffic.free_init.queue";

    @Bean
    public Queue trafficFreeInitQueue(){
        return new Queue(trafficFreeInitQueue,true,false,false);
    }

    @Bean
    public Binding trafficFreeInitBinding(){
        return BindingBuilder.bind(trafficFreeInitQueue()).to(trafficEventExchange())
                .with(trafficFreeInitRoutingKey);
    }





}
