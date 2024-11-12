package org.omni.toolkit.design.mq.topic.balance;

import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.consumer.Consumer;
import org.omni.toolkit.design.mq.producer.Producer;

import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Xieningjun
 * @date 2024/11/12 10:10
 * @description
 */
public interface Rebalance<T> {

    Map<Queue<Event<T>>, Consumer<T>> consumerRebalance(List<Queue<Event<T>>> queueList, List<Consumer<T>> consumerList);

    Map<Queue<Event<T>>, Producer<T>> producerRebalance(List<Queue<Event<T>>> queueList, List<Producer<T>> producerList);

}
