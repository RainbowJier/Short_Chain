package com.example.dcloud_link.config;

import lombok.Data;
import org.springframework.amqp.core.*;
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
    /**
     * 消息的routingKey.
     */
    private String shortLinkAddRoutingKey = "short_link.add.link.mapping.routing.key";

    private String shortLinkDelRoutingKey = "short_link.del.link.mapping.routing.key";

    private String shortLinkUpdateRoutingKey = "short_link.update.link.mapping.routing.key";

    /**
     * 交换机名称
     */
    private String shortLinkEventExchange = "short_link.event.exchange";

    /**
     * 创建交换机 Topic类型,⼀般⼀个微服务⼀个交换机
     * 必须要有消费者，才会创建交换机（懒加载）
     * durable = true：消息持久化，RabbitMQ重启后交换机依然存在，消息不会丢失
     * autoDelete = false：自动删除，当最后一个绑定解除后，RabbitMQ 不会自动删除该交换机
     */
    @Bean
    public Exchange shortLinkEventExchange() {
        return new TopicExchange(shortLinkEventExchange, true, false);
    }

    /**
     * ----------------------------- C 端新增短链队列 -----------------------------
     */
    // 新增短链队列名称
    private String shortLinkAddLinkQueue = "short_link.add.link.queue";

    // topic 类型的 binding key，用于绑定队列和交换机，是用于 C 端新增短链消费者
    private String shortLinkAddLinkBindingKey = "short_link.add.link.*.routing.key";

    // 删除
    private String shortLinkDelLinkQueue = "short_link.delete.link.queue";
    private String shortLinkDelLinkBindingKey = "short_link.delete.link.*.routing.key";

    // 更新
    private String shortLinkUpdateLinkQueue = "short_link.update.link.queue";
    private String shortLinkUpdateLinkBindingKey = "short_link.update.link.*.routing.key";

    /**
     * 创建新增队列，设置持久化，不自动删除
     */
    @Bean
    public Queue shortLinkAddLinkQueue() {
        return new Queue(shortLinkAddLinkQueue, true, false, false);
    }

    /**
     * 新增队列绑定到交换机
     */
    @Bean
    public Binding shortLinkAddApiBinding() {
        return BindingBuilder
                .bind(shortLinkAddLinkQueue())                        // 绑定队列
                .to(shortLinkEventExchange())                         // 绑定到交换机
                .with(shortLinkAddLinkBindingKey)                     // 路由键
                .noargs();                                            // 无额外参数
    }

    // 删除
    @Bean
    public Queue shortLinkDelLinkQueue() {
        return new Queue(shortLinkDelLinkQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkDelApiBinding() {
        return BindingBuilder
                .bind(shortLinkDelLinkQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkDelLinkBindingKey)
                .noargs();
    }

    // 更新
    @Bean
    public Queue shortLinkUpdateLinkQueue() {
        return new Queue(shortLinkUpdateLinkQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkUpdateApiBinding() {
        return BindingBuilder
                .bind(shortLinkUpdateLinkQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkUpdateLinkBindingKey)
                .noargs();
    }

    /**
     * ----------------------------- B 端新增短链队列 -----------------------------
     */
    // 新增队列名称
    private String shortLinkAddMappingQueue = "short_link.add.mapping.queue";

    // topic类型的binding key，⽤于绑定队列和交换机，是用于 mapping 消费者
    private String shortLinkAddMappingBindingKey = "short_link.add.*.mapping.routing.key";

    // 删除
    private String shortLinkDelMappingQueue = "short_link.delete.mapping.queue";
    private String shortLinkDelMappingBindingKey = "short_link.delete.*.mapping.routing.key";

    // 更新
    private String shortLinkUpdateMappingQueue = "short_link.update.mapping.queue";
    private String shortLinkUpdateMappingBindingKey = "short_link.update.*.mapping.routing.key";

    /**
     * 新增短链 mapping 普通队列，用于被监听，设置为可持久化
     */
    @Bean
    public Queue shortLinkAddMappingQueue() {
        return new Queue(shortLinkAddMappingQueue, true, false, false);
    }

    /**
     * 新增短链 mapping 队列和交换机的绑定关系建⽴
     */
    @Bean
    public Binding shortLinkAddMappingBinding() {
        return BindingBuilder
                .bind(shortLinkAddMappingQueue())                     // 绑定队列
                .to(shortLinkEventExchange())                         // 绑定到交换机
                .with(shortLinkAddMappingBindingKey)                  // 路由键
                .noargs();                                            // 无额外参数
    }


    // 删除
    @Bean
    public Queue shortLinkDelMappingQueue() {
        return new Queue(shortLinkDelMappingQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkDelMappingBinding() {
        return BindingBuilder
                .bind(shortLinkDelMappingQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkDelMappingBindingKey)
                .noargs();
    }

    // 更新
    @Bean
    public Queue shortLinkUpdateMappingQueue() {
        return new Queue(shortLinkUpdateMappingQueue, true, false, false);
    }

    @Bean
    public Binding shortLinkUpdateMappingBinding() {
        return BindingBuilder
                .bind(shortLinkUpdateMappingQueue())
                .to(shortLinkEventExchange())
                .with(shortLinkUpdateMappingBindingKey)
                .noargs();
    }
}
