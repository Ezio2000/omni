package org.omni.toolkit.design.mq.topic;

import lombok.Setter;
import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.producer.Producer;
import org.omni.toolkit.design.mq.topic.balance.Rebalance;
import org.omni.toolkit.design.mq.consumer.Consumer;
import org.omni.toolkit.design.mq.topic.push.Push;
import org.omni.toolkit.ex.ForbiddenException;
import org.omni.toolkit.vir.Virs;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Xieningjun
 * @date 2024/11/11 16:20
 * @description
 */
public class PushTopic<T> implements MsgQueue<T> {

    protected final List<Queue<Event<T>>> queueList = new CopyOnWriteArrayList<>();

    protected final List<Consumer<T>> consumerList = new CopyOnWriteArrayList<>();

    protected final Map<Queue<Event<T>>, Consumer<T>> consumerSubscribeMap = new ConcurrentHashMap<>();

    protected final List<Producer<T>> producerList = new CopyOnWriteArrayList<>();

    protected final Map<Queue<Event<T>>, Producer<T>> producerSubscribeMap = new ConcurrentHashMap<>();

    protected final AtomicInteger queueCursor = new AtomicInteger(0);

    @Setter
    protected Rebalance<T> rebalance;

    @Setter
    protected Push<T> push;

    public PushTopic(int queueNum) {
        for (int i = 0; i < queueNum; i++) {
            queueList.add(new ConcurrentLinkedQueue<>());
        }
    }

    @Override
    public void subscribe(Consumer<T> consumer) {
        consumerList.add(consumer);
        Virs.one(this::rebalance);
    }

    @Override
    public void unsubscribe(Consumer<T> consumer) {
        consumerList.remove(consumer);
        Virs.one(this::rebalance);
    }

    @Override
    public void subscribe(Producer<T> producer) {
        producerList.add(producer);
        Virs.one(this::rebalance);
    }

    @Override
    public void unsubscribe(Producer<T> producer) {
        producerList.remove(producer);
        Virs.one(this::rebalance);
    }

    @Override
    public void submit(Event<T> event) {
        final var index = queueCursor.getAndIncrement() % queueList.size();
        var queue = queueList.get(index);
        queue.add(event);
    }

    @Override
    public void submit(Event<T> event, int index) {
        var queue = queueList.get(index);
        queue.add(event);
    }

    // todo 调一次就行
    @Override
    public synchronized void push() {
        push.consumerPush(consumerSubscribeMap);
        push.producerPush(producerSubscribeMap);
    }

    @Override
    public Event<T> pull() {
        // todo 还没写完
        throw new ForbiddenException("Push topic can't be pull.");
    }

    // todo NULL的情况还没考虑进去
    private synchronized void rebalance() {
        // todo 建立在queueList不能变的前提
        // todo 这里释放了太多对象给别人改，没做好封闭性
        // 这里必须要单线程？
        consumerSubscribeMap.putAll(rebalance.consumerRebalance(queueList, consumerList));
        producerSubscribeMap.putAll(rebalance.producerRebalance(queueList, producerList));
        push();
    }

}
