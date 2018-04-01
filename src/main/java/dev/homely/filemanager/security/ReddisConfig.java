package dev.homely.filemanager.security;


import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class ReddisConfig {


    public RedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }


    public RedisConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory();
    }
}
