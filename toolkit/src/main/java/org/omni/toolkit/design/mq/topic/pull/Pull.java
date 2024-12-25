package org.omni.toolkit.design.mq.topic.pull;

import org.omni.toolkit.design.event.Event;
import org.omni.toolkit.design.mq.consumer.Consumer;
import org.omni.toolkit.design.mq.producer.Producer;

import java.util.Map;
import java.util.Queue;

/**
 * @author Xieningjun
 * @date 2024/12/25 11:27
 * @description 
 */
public interface Pull<T> {

    void consumerPull(Map<Queue<Event<T>>, Consumer<T>> subscribeMap);

    void producerPull(Map<Queue<Event<T>>, Producer<T>> subscribeMap);

}
