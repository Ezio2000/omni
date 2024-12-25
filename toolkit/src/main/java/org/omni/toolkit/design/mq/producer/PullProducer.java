package org.omni.toolkit.design.mq.producer;

import org.omni.toolkit.design.event.Event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Xieningjun
 * @date 2024/12/25 10:39
 * @description
 */
public class PullProducer<T> implements Producer<T> {

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
