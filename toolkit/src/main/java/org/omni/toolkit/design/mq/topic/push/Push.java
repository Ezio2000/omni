package org.omni.toolkit.design.mq.topic.push;

import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.consumer.Consumer;
import org.omni.toolkit.design.mq.producer.Producer;

import java.util.Map;
import java.util.Queue;

/**
 * @author Xieningjun
 * @date 2024/11/12 10:07
 * @description
 */
public interface Push<T> {

    void consumerPush(Map<Queue<Event<T>>, Consumer<T>> subscribeMap);

    void producerPush(Map<Queue<Event<T>>, Producer<T>> subscribeMap);

}
