package org.omni.redis.client;

import lombok.Setter;
import org.omni.redis.command.DangerCommand;
import org.omni.redis.manager.RedisCommandManager;
import org.omni.toolkit.sug.Sugars;
import org.omni.toolkit.vir.Virs;
import redis.clients.jedis.Connection;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author Xieningjun
 * @date 2024/11/7 19:31
 * @description
 */
public class DangerRedis extends Redis implements DangerCommand {

    private JedisMonitor monitor;

    @Setter
    private RedisCommandManager redisCommandManager;

    public DangerRedis(String host, int port, String password) {
        super(host, port, password);
    }

    @Override
    public synchronized MonitorProcess monitor() {
        Sugars.$ifNotNull$throw(monitor, new IllegalAccessError("Monitor already registered."));
        var process = new ConnectionMonitorProcess();
        if (monitor == null) {
            monitor = new ProcessJedisMonitor() {{
                setProcess(process);
                setRedisCommandManager(redisCommandManager);
                setHost(host);
            }};
        }
        Virs.one(() -> {
            try (var jedis = pool.getResource()) {
                jedis.monitor(monitor);
            }
        });
        return process;
    }

    @Setter
    public static class ConnectionMonitorProcess implements MonitorProcess {
        private Connection conn;

        @Override
        public void close() {
            Sugars.$ifNull$throw(conn, new IllegalAccessError("Process is not ready."));
            conn.close();
        }
    }

    @Setter
    private static class ProcessJedisMonitor extends JedisMonitor {
        private ConnectionMonitorProcess process;
        private RedisCommandManager redisCommandManager;
        private String host;

        @Override
        public void onCommand(String command) {
            Virs.one(() -> {
                var parts = command.split("\\s+");
                if (parts.length > 4) {
                    var client = parts[2].split(":")[0];
                    var commandName = parts[3].replaceAll("\"", "");
//                    log.debug("Client: {}, Command: {}", client, commandName);
                    redisCommandManager.insert(host, client, commandName);
                }
            });
        }

        @Override
        public void proceed(Connection conn) {
            process.setConn(conn);
            try {
                super.proceed(conn);
            } catch (JedisConnectionException e) {
//                log.info("Monitor connection closed.");
            }
        }
    }

}
