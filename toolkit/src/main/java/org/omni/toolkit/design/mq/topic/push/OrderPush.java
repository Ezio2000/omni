package org.omni.toolkit.design.mq.topic.push;

import lombok.extern.slf4j.Slf4j;
import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.consumer.Consumer;
import org.omni.toolkit.design.mq.producer.Producer;
import org.omni.toolkit.vir.Virs;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xieningjun
 * @date 2024/11/12 10:46
 * @description
 */
@Slf4j
public class OrderPush<T> implements Push<T> {

    protected final Map<Consumer<T>, Virs.LoopFuture> consumerPushFutureMap = new ConcurrentHashMap<>();

    protected final Map<Producer<T>, Virs.LoopFuture> producerPushFutureMap = new ConcurrentHashMap<>();

    private final boolean isOrder;

    public OrderPush(boolean isOrder) {
        this.isOrder = isOrder;
    }

    @Override
    public synchronized void consumerPush(Map<Queue<Event<T>>, Consumer<T>> subscribeMap) {
        for (var future : consumerPushFutureMap.values()) {
            future.close();
        }
        consumerPushFutureMap.clear();
        for (var entry : subscribeMap.entrySet()) {
            var queue = entry.getKey();
            var consumer = entry.getValue();
            Virs.LoopFuture future;
            // 消费速度最高为1000rps
            future = Virs.loop(() -> {
                var event = queue.poll();
                if (event != null) {
                    consumer.consume(event);
                }
            }, -1, 1, isOrder);
            consumerPushFutureMap.put(consumer, future);
        }
    }

    @Override
    public void producerPush(Map<Queue<Event<T>>, Producer<T>> subscribeMap) {
        for (var future : producerPushFutureMap.values()) {
            future.close();
        }
        producerPushFutureMap.clear();
        for (var entry : subscribeMap.entrySet()) {
            var queue = entry.getKey();
            var producer = entry.getValue();
            Virs.LoopFuture future;
            // 消费速度最高为1000rps
            future = Virs.loop(() -> {
                var event = producer.getEvent();
                if (event != null) {
                    queue.add(event);
                }
            }, -1, 1, isOrder);
            producerPushFutureMap.put(producer, future);
        }
    }

}
