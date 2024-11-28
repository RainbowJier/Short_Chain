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
     * 过期时间，单位：毫秒
     */
    private Integer ttl = 1000 * 60;

    /**
     * --------------交换机----------------
     */
    private String orderEventExchange = "order.event.exchange";

    @Bean
    public Exchange shortLinkEventExchange() {
        return new TopicExchange(orderEventExchange, true, false);
    }

    /**
     * 死信队列
     */
    private String orderCloseQueue = "order.close.queue";

    private String orderCloseRoutingKey = "order.close.routing.key";

    @Bean
    public Queue orderCloseQueue() {
        return new Queue(orderCloseQueue, true, false, false);
    }

    @Bean
    public Binding orderCloseBinding() {
        return BindingBuilder
                .bind(orderCloseQueue())
                .to(shortLinkEventExchange())
                .with(orderCloseRoutingKey)
                .noargs();
    }

    /**
     * 延迟队列
     */
    private String orderCloseDelayQueue = "order.close.delay.queue";

    private String orderCloseDelayRoutingKey = "order.close.delay.routing.key";

    /**
     * 设置消息的过期时间，单位：毫秒
     * 过期后，使用指定的routing key，将消息发送到指定的交换机
     */
    @Bean
    public Queue orderCloseDelayQueue() {
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", orderEventExchange);  // 死信交换机
        arguments.put("x-dead-letter-routing-key", orderCloseRoutingKey); // 死信路由键
        arguments.put("x-message-ttl", ttl);    // 过期时间

        return new Queue(orderCloseDelayQueue, true, false, false,arguments);
    }

    // 交换机绑定队列
    @Bean
    public Binding orderCloseDelayBinding() {
        return BindingBuilder
                .bind(orderCloseDelayQueue())
                .to(shortLinkEventExchange())
                .with(orderCloseDelayRoutingKey)
                .noargs();
    }

    /**
     * 消息转换器，将延迟消息 -> 死信交换机 -> 死信队列
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
