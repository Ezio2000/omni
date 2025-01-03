package org.omni.nacos.manager;

import org.omni.toolkit.empty.EmptyObj;
import org.omni.toolkit.operate.MapHashOperate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xieningjun
 * @date 2024/11/4 17:57
 * @description 存放配置文件字段
 */
public class NacosValueManager extends MapHashOperate<String, String, Object> {

    @Override
    public void insert(String key, String hKey) {
        operate.computeIfAbsent(key, k -> new ConcurrentHashMap<>()).putIfAbsent(hKey, EmptyObj.of());
    }

}
