package org.omni.toolkit.design.mq.producer;

import org.omni.toolkit.design.mq.event.Event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Xieningjun
 * @date 2024/11/12 13:41
 * @description
 */
// push和pull 是指topic对producer而言
public class PushProducer<T> implements Producer<T> {

    private final Queue<Event<T>> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void produce(Event<T> event) {
        queue.add(event);
    }

    @Override
    public Event<T> getEvent() {
        return queue.poll();
    }

}
