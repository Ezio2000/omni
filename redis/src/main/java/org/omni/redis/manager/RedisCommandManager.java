package org.omni.redis.manager;

import org.omni.toolkit.operate.MapHashOperate;
import org.omni.toolkit.struc.queue.BoundedConcurrentLinkedQueue;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xieningjun
 * @date 2024/11/8 12:17
 * @description
 */
public class RedisCommandManager extends MapHashOperate<String, String, Queue<String>> {

    // todo 跟自动清除会不会有冲突，有可能会丢
    public void insert(String key, String hKey, String command) {
        // 只记录近10000个操作
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(hKey, k -> new BoundedConcurrentLinkedQueue<>(10000))
                .add(command);
    }

}
