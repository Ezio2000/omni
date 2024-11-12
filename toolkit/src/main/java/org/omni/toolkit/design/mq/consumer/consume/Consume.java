package org.omni.toolkit.design.mq.consumer.consume;

import org.omni.toolkit.design.mq.event.Event;

import java.util.Queue;

/**
 * @author Xieningjun
 * @date 2024/11/12 12:24
 * @description
 */
public interface Consume<T> {

    void consume(Queue<Event<T>> queue);

}
