package org.omni.toolkit.design.mq.topic.balance;

import org.omni.toolkit.design.event.Event;
import org.omni.toolkit.design.mq.consumer.Consumer;
import org.omni.toolkit.design.mq.producer.Producer;

import java.util.*;

/**
 * @author Xieningjun
 * @date 2024/11/12 10:13
 * @description
 */
public class HashRebalance<T> implements Rebalance<T> {

    @Override
    public Map<Queue<Event<T>>, Consumer<T>> consumerRebalance(List<Queue<Event<T>>> queueList, List<Consumer<T>> consumerList) {
        var subscribeMap = new HashMap<Queue<Event<T>>, Consumer<T>>();
        consumerList.sort(Comparator.comparingInt(Object::hashCode));
        for (int i = 0; i < queueList.size(); i++) {
            var queue = queueList.get(i);
            subscribeMap.put(queue, consumerList.get(i % consumerList.size()));
        }
        return subscribeMap;
    }

    @Override
    public Map<Queue<Event<T>>, Producer<T>> producerRebalance(List<Queue<Event<T>>> queueList, List<Producer<T>> producerList) {
        var subscribeMap = new HashMap<Queue<Event<T>>, Producer<T>>();
        producerList.sort(Comparator.comparingInt(Object::hashCode));
        for (int i = 0; i < queueList.size(); i++) {
            var queue = queueList.get(i);
            subscribeMap.put(queue, producerList.get(i % producerList.size()));
        }
        return subscribeMap;
    }

}

