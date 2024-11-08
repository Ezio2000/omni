//package org.omni.redis.toolkit;
//
//import lombok.Setter;
//import org.omni.redis.manager.RedisCommandManager;
//import org.omni.redis.manager.RedisInfoManager;
//import org.omni.toolkit.struc.custom.Tuple;
//import org.omni.toolkit.vir.Virs;
//
//import java.util.Vector;
//
///**
// * @author Xieningjun
// * @date 2024/11/8 14:26
// * @description
// */
//public class RedisManagerConsumes {
//
//    public static void consumeRedisCommandManager(RedisCommandManager manager, String host, ConsumeStrategy<String> strategy) {
//        var map = manager.query(host);
//        var existsClientList = new Vector<>();
//        Virs.loop(() -> {
//            for (var client : map.keySet()) {
//                if (!existsClientList.contains(client)) {
//                    existsClientList.add(client);
//                    consumeRedisCommandManager(manager, host, client, strategy);
//                }
//            }
//        }, -1, 1000 /* 遍历长度超过1s的话会有多个线程监视同一个queue */, true);
//    }
//
//    public static void consumeRedisCommandManager(RedisCommandManager manager, String host, String client, ConsumeStrategy<String> strategy) {
//        var queue = manager.query(host, client);
//        Virs.loop(() -> {
//            var command = queue.poll();
//            strategy.consume(command);
//        }, -1, strategy.speed, strategy.order);
//    }
//
//    public static void consumeRedisInfoManager(RedisInfoManager manager, String host, ConsumeStrategy<Tuple<String, String>> strategy) {
//        var map = manager.query(host);
//        Virs.loop(() -> {
//            for (var entry : map.entrySet()) {
//                strategy.consume(new Tuple<>(entry.getKey(), entry.getValue()));
//            }
//        }, -1, strategy.speed, true);
//    }
//
//    public abstract static class ConsumeStrategy<T> {
//        @Setter
//        private long speed;
//        @Setter
//        private boolean order;
//        protected abstract void consume(T t);
//    }
//
//}
