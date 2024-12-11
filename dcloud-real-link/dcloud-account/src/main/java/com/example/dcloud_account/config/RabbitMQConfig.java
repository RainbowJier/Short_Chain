package com.example.dcloud_account.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
     * 消息转换器，设置消息的序列化方式
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
