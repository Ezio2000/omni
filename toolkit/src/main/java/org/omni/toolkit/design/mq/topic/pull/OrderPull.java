package org.omni.toolkit.design.mq.topic.pull;

import org.omni.toolkit.design.event.Event;
import org.omni.toolkit.design.mq.consumer.Consumer;
import org.omni.toolkit.design.mq.consumer.PullConsumer;
import org.omni.toolkit.design.mq.producer.Producer;
import org.omni.toolkit.design.mq.producer.PullProducer;
import org.omni.toolkit.vir.Virs;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Xieningjun
 * @date 2024/12/25 11:27
 * @description pull必须是按顺序拉取，比如说:调用消费者的pull()方法，规定只能获取一条，那只能是顺序的
 */
public class OrderPull<T> implements Pull<T> {

    protected final Map<Queue<Event<T>>, Virs.LoopFuture> producerPullFutureMap = new ConcurrentHashMap<>();

    private final boolean isOrder;

    public OrderPull(boolean isOrder) {
        this.isOrder = isOrder;
    }

    @Override
    public void consumerPull(Map<Queue<Event<T>>, Consumer<T>> subscribeMap) {
        for (var entry : subscribeMap.entrySet()) {
            var queue = entry.getKey();
            var consumer = entry.getValue();
            if (consumer instanceof PullConsumer<T>) {
                ((PullConsumer<T>) consumer).addTopicQueue(queue);
            }
        }
    }

    @Override
    public void producerPull(Map<Queue<Event<T>>, Producer<T>> subscribeMap) {
        for (var future : producerPullFutureMap.values()) {
            future.close();
        }
        producerPullFutureMap.clear();
        for (var entry : subscribeMap.entrySet()) {
            var queue = entry.getKey();
            var producer = entry.getValue();
            if (producer instanceof PullProducer<T> && !producerPullFutureMap.containsKey(queue)) {
                // 消费速度最高为1000rps
                var future = Virs.loop(() -> {
                    var event = producer.getEvent();
                    if (event != null) {
                        queue.add(event);
                    }
                }, -1, 1, isOrder);
                producerPullFutureMap.put(queue, future);
            }
        }
    }

}
