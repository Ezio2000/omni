import org.junit.Test;
import org.omni.redis.client.DangerRedis;
import org.omni.redis.client.Redis;
import org.omni.redis.manager.RedisCommandManager;
import org.omni.redis.manager.RedisInfoManager;
import org.omni.toolkit.vir.Virs;

import java.io.IOException;

/**
 * @author Xieningjun
 * @date 2024/11/7 18:10
 * @description
 */
public class RedisTest {

    @Test
    public void info() {
        var redisInfoManager = new RedisInfoManager();
        var redis = new Redis(
                "10.0.2.23",
                6379,
                "gac_travel_test_new@NJElna9OCLisAi#5RfY8oi#M1Qp71ZX"
        );
        redis.setRedisInfoManager(redisInfoManager);
        redis.register();
        Virs.sleep(1000);
        System.out.println(redis.ping());
        System.out.println(redisInfoManager.query("10.0.2.23", "maxmemory_human"));
    }

    @Test
    public void monitor() throws IOException {
        var redisCommandManager = new RedisCommandManager();
        var redis = new DangerRedis(
                "10.0.2.23",
                6379,
                "gac_travel_test_new@NJElna9OCLisAi#5RfY8oi#M1Qp71ZX"
        );
        redis.setRedisCommandManager(redisCommandManager);
        redis.register();
        var process = redis.monitor();
        Virs.sleep(5000);
        process.close();
        System.out.println(redisCommandManager.query("10.0.2.23"));
        Virs.keepalive();
    }

}
