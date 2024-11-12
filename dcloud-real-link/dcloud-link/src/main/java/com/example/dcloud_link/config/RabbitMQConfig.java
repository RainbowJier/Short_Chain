package com.example.dcloud_link.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
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

    /**
     * 交换机
     */
    private String shortLinkEventExchange = "short_link.event.exchange";

    /**
     * 创建交换机 Topic类型
     * ⼀般⼀个微服务⼀个交换机
     * 必须要有消费者，才会创建交换机（懒加载）
     */
    @Bean
    public Exchange shortLinkEventExchange() {
        // durable = true：消息持久化，RabbitMQ重启后交换机依然存在，消息不会丢失
        // autoDelete = false：自动删除，当最后一个绑定解除后，RabbitMQ 不会自动删除该交换机
        return new TopicExchange(shortLinkEventExchange, true, false);
    }

    /**
     * 新增短链具体的routingKey,【发送消息使⽤】
     */
    private String shortLinkAddRoutingKey = "short_link.add.link.mapping.routing.key";


    // ####### C 端新增短链相关配置 #######
    /**
     * 新增短链队列
     */
    private String shortLinkAddLinkQueue = "short_link.add.link.queue";

    /**
     * topic 类型的 binding key，用于绑定队列和交换机，是用于 C 端新增短链消费者
     */
    private String shortLinkAddLinkBindingKey = "short_link.add.link.*.routing.key";

    /**
     * 新增短链 api 队列和交换机的绑定关系建⽴
     */
    @Bean
    public Binding shortLinkAddApiBinding() {
        return new Binding(shortLinkAddLinkQueue, Binding.DestinationType.QUEUE,
                shortLinkEventExchange, shortLinkAddLinkBindingKey, null);
    }

    /**
     * 新增短链 api 普通队列，⽤于被监听
     */
    @Bean
    public Queue shortLinkAddLinkQueue() {
        return new Queue(shortLinkAddLinkQueue, true, false, false);
    }


    // B 端新增短链映射相关配置
    /**
     * 新增短链映射 队列
     */
    private String shortLinkAddMappingQueue = "short_link.add.mapping.queue";

    /**
     * topic类型的binding key，⽤于绑定队列和交换机，是用于 mapping 消费者
     */
    private String shortLinkAddMappingBindingKey = "short_link.add.*.mapping.routing.key";


    /**
     * 新增短链 mapping 队列和交换机的绑定关系建⽴
     */
    @Bean
    public Binding shortLinkAddMappingBinding() {
        return new Binding(shortLinkAddMappingQueue, Binding.DestinationType.QUEUE,
                shortLinkEventExchange, shortLinkAddMappingBindingKey, null);
    }

    /**
     * 新增短链 mapping 普通队列，用于被监听
     * 设置为可持久化
     */
    @Bean
    public Queue shortLinkAddMappingQueue() {
        return new Queue(shortLinkAddMappingQueue, true, false, false);
    }
}
