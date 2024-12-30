package com.example.dcloud_shop.config;

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

@Configuration
@Data
public class RabbitMQConfig {
    /**
     * expired time = 1 minute, unit: milliseconds
     */
    private Integer ttl = 1000 * 60 * 1;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Exchange.
     */
    private String orderEventExchange = "order.event.exchange";

    @Bean
    public TopicExchange orderEventExchange() {
        return new TopicExchange(orderEventExchange, true, false);
    }

    //=============Failed to pay and close order===================
    private String orderCloseQueue = "order.close.queue";

    private String orderCloseRoutingKey = "order.close.routing.key";

    @Bean
    public Queue orderCloseQueue() {
        return new Queue(orderCloseQueue, true, false, false);
    }

    @Bean
    public Binding orderCloseBinding() {
        return BindingBuilder.bind(orderCloseQueue())
                .to(orderEventExchange())
                .with(orderCloseRoutingKey);
    }

    private String orderCloseDelayQueue = "order.close.delay.queue";

    private String orderCloseDelayRoutingKey = "order.close.delay.routing.key";

    @Bean
    public Queue orderCloseDelayQueue() {
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", orderEventExchange);
        arguments.put("x-dead-letter-routing-key", orderCloseRoutingKey);
        arguments.put("x-message-ttl", ttl);

        return new Queue(orderCloseDelayQueue, true, false, false, arguments);
    }

    @Bean
    public Binding orderCloseDelayBinding() {
        return BindingBuilder.bind(orderCloseDelayQueue())
                .to(orderEventExchange())
                .with(orderCloseDelayRoutingKey);
    }

    //=============Payment Success and update traffic status==================
    private String orderUpdateTrafficRoutingKey = "order.update.traffic.routing.key";

    private String orderUpdateQueue = "order.update.queue";

    private String orderUpdateBindingKey = "order.update.*.routing.key";

    @Bean
    public Queue orderUpdateQueue() {
        return new Queue(orderUpdateQueue, true, false, false);
    }

    @Bean
    public Binding orderUpdateBinding() {
        return BindingBuilder.bind(orderUpdateQueue())
                .to(orderEventExchange())
                .with(orderUpdateBindingKey);
    }

    private String orderTrafficQueue = "order.traffic.queue";

    private String orderTrafficBindingKey = "order.*.traffic.routing.key";

    @Bean
    public Queue orderTrafficQueue() {
        return new Queue(orderTrafficQueue, true, false, false);
    }

    @Bean
    public Binding orderTrafficBinding() {
        return BindingBuilder.bind(orderTrafficQueue())
                .to(orderEventExchange())
                .with(orderTrafficBindingKey);
    }

}
