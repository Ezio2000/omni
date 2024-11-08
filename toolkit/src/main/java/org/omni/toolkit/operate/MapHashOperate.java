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
    public void insert(K key, HK hKey, V value) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).putIfAbsent(hKey, value);
    }

    @Override
    public void insert(K key, HK hKey) {
        // todo 怎么处理EmptyObj?
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).putIfAbsent(hKey, (V) EmptyObj.of());
    }

    @Override
    public void insert(K key, Map<HK, V> map) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).putAll(map);
    }

    @Override
    public void delete(K key, HK hKey) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).remove(hKey);
    }

    @Override
    public void delete(K key) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).clear();
    }

    // todo 接入springboot的时候 可以给它加代理
    @Override
    public void update(K key, HK hKey, V value) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).put(hKey, value);
    }

    @Override
    public V query(K key, HK hKey) {
        return operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).get(hKey);
    }

    @Override
    public Map<HK, V> query(K key) {
        return operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
    }

    @Override
    public boolean exists(K key, HK hKey) {
        return operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).containsKey(hKey);
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
