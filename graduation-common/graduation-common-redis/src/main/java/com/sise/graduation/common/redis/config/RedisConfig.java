package com.sise.graduation.common.redis.config;

import com.sise.graduation.common.redis.serializer.FastjsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {

    @Bean
    public RedisSerializer fastJsonSerializer() {
        return new FastjsonSerializer<>(Object.class);
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory, RedisSerializer fastJsonSerializer){
        RedisTemplate redisTemplate = new StringRedisTemplate(connectionFactory);
        redisTemplate.setValueSerializer(fastJsonSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
