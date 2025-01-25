package org.omni.http.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author Xieningjun
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    @Qualifier("jedisPool1")
    private JedisPool jedisPool1;

    @Autowired
    @Qualifier("jedisPool2")
    private JedisPool jedisPool2;

    @RequestMapping("/test")
    public void test() {
        log.info("收到请求，执行线程是：{}", Thread.currentThread());
    }

    @RequestMapping("/db1")
    public String db1() {
        try (var conn = jedisPool1.getResource()) {
            log.info(conn.get("myTestKey"));
            return conn.set("myTestKey", "myTestValue1");
        }
    }

    @RequestMapping("/db2")
    public String db2() {
        try (var conn = jedisPool2.getResource()) {
            log.info(conn.get("myTestKey"));
            return conn.set("myTestKey", "myTestValue2");
        }
    }

}
