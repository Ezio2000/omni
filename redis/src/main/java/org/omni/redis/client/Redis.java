package org.omni.redis.client;

import lombok.Setter;
import org.omni.redis.command.ServerCommand;
import org.omni.redis.manager.RedisInfoManager;
import org.omni.toolkit.sug.Sugars;
import org.omni.toolkit.vir.Virs;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xieningjun
 */
public class Redis implements ServerCommand {

    protected final String host;

    protected final int port;

    protected final String password;

    @Setter
    protected RedisInfoManager redisInfoManager = new RedisInfoManager();

    protected JedisPool pool;

    public Redis(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public synchronized void register() {
        Sugars.$ifNotNull$throw(pool, new IllegalAccessError("Redis already registered."));
        var poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1);
        poolConfig.setMaxIdle(1);
        pool = new JedisPool(poolConfig, host, port, 2000, password);
        updateInfo();
        Virs.loop(this::updateInfo, -1, 10000, true);
    }

    @Override
    public String info() {
        try (var jedis = pool.getResource()) {
            return jedis.info();
        }
    }

    @Override
    public String ping() {
        try (var jedis = pool.getResource()) {
            return jedis.ping();
        }
    }

    private synchronized void updateInfo() {
        redisInfoManager.insert(host, parseInfo(info()));
    }

    /**
     * 解析 Redis INFO 输出并将其转换为 Map
     * @param info Redis INFO 命令的输出
     * @return 返回 Map，键是字段名，值是对应的字段值
     */
    private Map<String, String> parseInfo(String info) {
        var infoMap = new HashMap<String, String>();
        // 按行拆分 INFO 输出
        var lines = info.split("\n");
        for (var line : lines) {
            // 跳过空行和以 '#' 开头的部分（这些是 INFO 输出中的注释）
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            // 将每行按冒号分隔，得到 key 和 value
            var parts = line.split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                infoMap.put(key, value);
            }
        }
        return infoMap;
    }

}
