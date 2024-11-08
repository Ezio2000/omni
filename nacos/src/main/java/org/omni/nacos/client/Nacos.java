package org.omni.nacos.client;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.config.impl.ConfigChangeHandler;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import lombok.Setter;
import org.omni.nacos.manager.NacosValueManager;
import org.omni.toolkit.sug.Sugars;
import org.omni.toolkit.vir.Virs;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Xieningjun
 */
public class Nacos {

    private final String serverAddr;

    private final String namespace;

    private final String cluster;

    private final String group;

    private final String serviceName;

    private final String ip;

    private final int port;

    private Instance current;

    @Setter
    private NacosValueManager nacosValueManager = new NacosValueManager();

    private NamingService namingService;

    private ConfigService configService;

    public Nacos(String serverAddr, String namespace, String cluster, String group, String serviceName, String ip, int port) {
        this.serverAddr = serverAddr;
        this.namespace = namespace;
        this.cluster = cluster;
        this.group = group;
        this.serviceName = serviceName;
        this.ip = ip;
        this.port = port;
        this.current = buildInstance();
    }

    public synchronized void register() throws NacosException {
        Sugars.$ifNotNull$throw(namingService, new IllegalAccessError("Service already registered."));
        var properties = new Properties();
        properties.setProperty("serverAddr", serverAddr);
        properties.setProperty("namespace", namespace);
        namingService = NacosFactory.createNamingService(properties);
        namingService.registerInstance(serviceName, group, current);
        configService = NacosFactory.createConfigService(properties);
    }

    // todo 不生效
    public synchronized void meta(String key, String val) {
        current.addMetadata(key, val);
    }

    public Instance discover(String serviceName) throws NacosException {
        return namingService.selectOneHealthyInstance(serviceName, group, Collections.singletonList(cluster));
    }

    // 加回调
    public void subscribe(String dataId) throws NacosException {
        var listener = new VirListener() {
            @Override
            public void receiveConfigChange(ConfigChangeEvent event) {
                for (var item : event.getChangeItems()) {
                    if (nacosValueManager.exists(dataId, item.getKey())) {
                        nacosValueManager.update(dataId, item.getKey(), item.getNewValue());
                    }
                }
            }
        };
        var config = configService.getConfigAndSignListener(dataId, group, 2000, listener);
        Virs.one(() -> {
            ConfigChangeEvent event;
            try {
                // todo 只支持yaml格式
                event = new ConfigChangeEvent(ConfigChangeHandler.getInstance().parseChangeData("", config, "yaml"));
            } catch (IOException e) {
                // todo 想想怎么处理
                throw new RuntimeException(e);
            }
            listener.receiveConfigChange(event);
        });
    }

    private synchronized Instance buildInstance() {
        var instance = new Instance();
        instance.setIp(ip);
        instance.setPort(port);
        instance.setClusterName(cluster);
        return instance;
    }

    private abstract static class VirListener extends AbstractConfigChangeListener {
        @Override
        public Executor getExecutor() {
            return Executors.newVirtualThreadPerTaskExecutor();
        }
    }

}
