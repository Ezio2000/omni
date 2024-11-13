package org.omni.redis.client;

import org.omni.redis.command.SafeCommand;

/**
 * @author Xieningjun
 * @date 2024/11/13 11:05
 * @description
 */
public class SafeRedis extends Redis implements SafeCommand {

    public SafeRedis(String host, int port, String password) {
        super(host, port, password);
    }

    @Override
    public String get(String key) {
        try (var jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }

    @Override
    public void set(String key, String value) {
        try (var jedis = pool.getResource()) {
            jedis.set(key, value);
        }
    }

    @Override
    public void del(String key) {
        try (var jedis = pool.getResource()) {
            jedis.del(key);
        }
    }

    @Override
    public boolean exists(String key) {
        try (var jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }

    @Override
    public void expire(String key, int seconds) {
        try (var jedis = pool.getResource()) {
            jedis.expire(key, seconds);
        }
    }

    @Override
    public long ttl(String key) {
        try (var jedis = pool.getResource()) {
            return jedis.ttl(key);
        }
    }

    @Override
    public long incr(String key) {
        try (var jedis = pool.getResource()) {
            return jedis.incr(key);
        }
    }

    @Override
    public long decr(String key) {
        try (var jedis = pool.getResource()) {
            return jedis.decr(key);
        }
    }

    @Override
    public String hget(String key, String field) {
        try (var jedis = pool.getResource()) {
            return jedis.hget(key, field);
        }
    }

    @Override
    public void hset(String key, String field, String value) {
        try (var jedis = pool.getResource()) {
            jedis.hset(key, field, value);
        }
    }

    @Override
    public void sadd(String key, String value) {
        try (var jedis = pool.getResource()) {
            jedis.sadd(key, value);
        }
    }

    @Override
    public void lpush(String key, String value) {
        try (var jedis = pool.getResource()) {
            jedis.lpush(key, value);
        }
    }

    @Override
    public void rpush(String key, String value) {
        try (var jedis = pool.getResource()) {
            jedis.rpush(key, value);
        }
    }

    @Override
    public String lpop(String key) {
        try (var jedis = pool.getResource()) {
            return jedis.lpop(key);
        }
    }

    @Override
    public String rpop(String key) {
        try (var jedis = pool.getResource()) {
            return jedis.rpop(key);
        }
    }

}
