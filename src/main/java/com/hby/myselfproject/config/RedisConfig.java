package com.hby.myselfproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
//@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private String timeout;

    @Bean
    public JedisPool getJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool pool = new JedisPool(config, host, Integer.parseInt(port),Integer.parseInt(timeout),password,1);
        return pool;
    }

    @Bean
    public Jedis getJedis(){
        JedisPool jedisPool = getJedisPool();
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }

}
