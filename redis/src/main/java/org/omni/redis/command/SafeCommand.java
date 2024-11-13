package org.omni.redis.command;

/**
 * @author Xieningjun
 * @date 2024/11/7 18:34
 * @description
 */
public interface SafeCommand {

    // 获取一个键的值
    String get(String key);

    // 设置一个键的值
    void set(String key, String value);

    // 删除一个键
    void del(String key);

    // 检查键是否存在
    boolean exists(String key);

    // 设置键的过期时间（秒）
    void expire(String key, int seconds);

    // 获取键的剩余过期时间（秒）
    long ttl(String key);

    // 增加键的整数值
    long incr(String key);

    // 减少键的整数值
    long decr(String key);

    // 获取哈希表中的字段
    String hget(String key, String field);

    // 设置哈希表中的字段
    void hset(String key, String field, String value);

    // 向集合添加一个元素
    void sadd(String key, String value);

    // 向列表添加元素
    void lpush(String key, String value);

    // 向列表添加元素
    void rpush(String key, String value);

    // 弹出列表中的元素
    String lpop(String key);

    // 获取并删除列表中的第一个元素
    String rpop(String key);

}
