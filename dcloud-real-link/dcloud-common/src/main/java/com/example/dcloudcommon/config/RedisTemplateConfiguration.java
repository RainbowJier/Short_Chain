package com.example.dcloudcommon.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description：RedisTemplate配置类
 * @Author： RainbowJier
 * @Data： 2024/9/22 14:05
 */


public class RedisTemplateConfiguration {

    /**
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用 Jackson2JsonRedisSerializer 替换默认的序列化方式
        // 这样对象可以直接序列化，而不需要实现 Serializable 接口
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 设置序列化时的可见性规则，这里允许所有属性进行序列化
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 将 ObjectMapper 设置到 Jackson2JsonRedisSerializer 中
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置 Redis 中键 (key) 的序列化规则，使用 StringRedisSerializer 对键进行序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        // 设置 Redis 中 hash 结构的键 (hashKey) 的序列化规则，使用 StringRedisSerializer 对 hashKey 进行序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        // 返回配置好的 RedisTemplate 实例
        return redisTemplate;

    }
}
