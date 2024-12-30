package com.example.dcloud_link.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: RainbowJier
 * @Description: 👺🐉😎
 * @Date: 2024/11/12 9:40
 * @Version: 1.0
 */

@Configuration
@Data
public class RabbitMQConfig {

    private String shortLinkAddRoutingKey = "short_link.add.link.mapping.routing.key";
    private String shortLinkDelRoutingKey = "short_link.delete.link.mapping.routing.key";
    private String shortLinkUpdateRoutingKey = "short_link.update.link.mapping.routing.key";

    /**
     * Exchange.
     */
    private String shortLinkEventExchange = "short_link.event.exchange";

    @Bean
    public TopicExchange shortLinkEventExchange() {
        return new TopicExchange(shortLinkEventExchange, true, false);
    }

    /**
     * ----------------------------- C Endpoint -----------------------------
     */
    // add
    private String shortLinkAddLinkBindingKey = "short_link.add.link.*.routing.key";
    private String shortLinkAddLinkQueue = "short_link.add.link.queue";

    @Bean
    public Queue shortLinkAddLinkQueue() {
        return new Queue(shortLinkAddLinkQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkAddApiBinding() {
        return BindingBuilder
                .bind(shortLinkAddLinkQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkAddLinkBindingKey);
    }

    // delete
    private String shortLinkDelLinkBindingKey = "short_link.delete.link.*.routing.key";
    private String shortLinkDelLinkQueue = "short_link.delete.link.queue";

    @Bean
    public Queue shortLinkDelLinkQueue() {
        return new Queue(shortLinkDelLinkQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkDelApiBinding() {
        return BindingBuilder
                .bind(shortLinkDelLinkQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkDelLinkBindingKey);
    }

    // update
    private String shortLinkUpdateLinkQueue = "short_link.update.link.queue";
    private String shortLinkUpdateLinkBindingKey = "short_link.update.link.*.routing.key";

    @Bean
    public Queue shortLinkUpdateLinkQueue() {
        return new Queue(shortLinkUpdateLinkQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkUpdateApiBinding() {
        return BindingBuilder
                .bind(shortLinkUpdateLinkQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkUpdateLinkBindingKey);
    }

    /**
     * ----------------------------- B Endpoint -----------------------------
     */
    // add
    private String shortLinkAddMappingBindingKey = "short_link.add.*.mapping.routing.key";
    private String shortLinkAddMappingQueue = "short_link.add.mapping.queue";

    @Bean
    public Queue shortLinkAddMappingQueue() {
        return new Queue(shortLinkAddMappingQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkAddMappingBinding() {
        return BindingBuilder
                .bind(shortLinkAddMappingQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkAddMappingBindingKey);
    }

    // delete
    private String shortLinkDelMappingQueue = "short_link.delete.mapping.queue";
    private String shortLinkDelMappingBindingKey = "short_link.delete.*.mapping.routing.key";

    @Bean
    public Queue shortLinkDelMappingQueue() {
        return new Queue(shortLinkDelMappingQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkDelMappingBinding() {
        return BindingBuilder
                .bind(shortLinkDelMappingQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkDelMappingBindingKey);
    }

    // update
    private String shortLinkUpdateMappingQueue = "short_link.update.mapping.queue";
    private String shortLinkUpdateMappingBindingKey = "short_link.update.*.mapping.routing.key";

    @Bean
    public Queue shortLinkUpdateMappingQueue() {
        return new Queue(shortLinkUpdateMappingQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkUpdateMappingBinding() {
        return BindingBuilder
                .bind(shortLinkUpdateMappingQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkUpdateMappingBindingKey);
    }
}
