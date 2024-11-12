package org.omni.toolkit.design.mq.producer.produce;

import org.omni.toolkit.design.mq.event.Event;

import java.util.Queue;

/**
 * @author Xieningjun
 * @date 2024/11/12 12:24
 * @description
 */
public interface Produce<T> {

    void produce(Queue<Event<T>> queue);

}
