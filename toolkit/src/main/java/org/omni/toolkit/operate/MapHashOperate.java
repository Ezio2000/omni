package org.omni.toolkit.operate;

import org.omni.toolkit.empty.EmptyObj;
import org.omni.toolkit.vir.Virs;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xieningjun
 */
public abstract class MapHashOperate<K, HK, V> implements HashOperate<K, HK, V> {

    protected Map<K, Map<HK, V>> operate = new ConcurrentHashMap<>();

    // todo 考虑一下这样会不会有问题？没清掉 或者多清理了什么的
    public MapHashOperate() {
        Virs.loop(() -> {
            for (var entry : operate.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    operate.remove(entry.getKey());
                }
            }
        }, -1, 10, true);
    }

    @Override
    public void insert(K key, HK hashKey, V value) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).putIfAbsent(hashKey, value);
    }

    @Override
    public void insert(K key, HK hashKey) {
        // todo 怎么处理EmptyObj?
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).putIfAbsent(hashKey, (V) EmptyObj.of());
    }

    @Override
    public void delete(K key, HK hashKey) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).remove(hashKey);
    }

    @Override
    public void delete(K key) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).clear();
    }

    @Override
    public void update(K key, HK hashKey, V value) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).put(hashKey, value);
    }

    @Override
    public V query(K key, HK hashKey) {
        return operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).get(hashKey);
    }

    @Override
    public Map<HK, V> query(K key) {
        return operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
    }

    @Override
    public boolean exists(K key, HK hashKey) {
        return operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).containsKey(hashKey);
    }

    @Override
    public boolean exists(K key) {
        return operate.containsKey(key);
    }

    @Override
    public Set<K> getKeys() {
        return operate.keySet();
    }

}
