package org.omni.toolkit.operate;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Xieningjun
 * @date 2024/11/4 18:12
 */
public interface HashOperate<K, HK, V> {

    void insert(K key, HK hashKey, V value);

    void insert(K key, HK hashKey);

    void insert(K key, Map<HK, V> map);

    void delete(K key, HK hashKey);

    void delete(K key);

    void update(K key, HK hashKey, V value);

    V query(K key, HK hashKey);

    Map<HK, V> query(K key);

    boolean exists(K key, HK hashKey);

    boolean exists(K key);

    Set<K> getKeys();

}
