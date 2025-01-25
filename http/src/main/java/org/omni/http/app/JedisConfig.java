package org.omni.http.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Xieningjun
 */
@Configuration
public class JedisConfig {

    @Bean(name = "jedisPool1")
    public JedisPool jedisPool1() {
        // 创建连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);   // 设置最大连接数
        poolConfig.setMaxIdle(5);     // 设置最大空闲连接数
        poolConfig.setMinIdle(1);     // 设置最小空闲连接数
        poolConfig.setTestOnBorrow(true); // 配置在借用连接时是否进行有效性检查
        poolConfig.setJmxEnabled(false);
        // 设置 Redis 主机、端口、密码和数据库
        return new JedisPool(poolConfig, "10.3.1.117", 6379, 2000,
                "gac_travel_test_new@NJElna9OCLisAi#5RfY8oi#M1Qp71ZX", 0);
    }

    @Bean(name = "jedisPool2")
    public JedisPool jedisPool2() {
        // 创建连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);   // 设置最大连接数
        poolConfig.setMaxIdle(5);     // 设置最大空闲连接数
        poolConfig.setMinIdle(1);     // 设置最小空闲连接数
        poolConfig.setTestOnBorrow(true); // 配置在借用连接时是否进行有效性检查
        poolConfig.setJmxEnabled(false);
        // 设置 Redis 主机、端口、密码和数据库
        return new JedisPool(poolConfig, "10.3.1.117", 6379, 2000,
                "gac_travel_test_new@NJElna9OCLisAi#5RfY8oi#M1Qp71ZX", 5);
    }

}
