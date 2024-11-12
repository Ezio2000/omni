package org.omni.toolkit.design.mq.consumer;

import lombok.Setter;
import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.consumer.consume.Consume;
import org.omni.toolkit.design.mq.consumer.consume.OrderConsume;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Xieningjun
 * @date 2024/11/12 11:16
 */
public class PushConsumer<T> implements Consumer<T> {

    private final Queue<Event<T>> queue = new ConcurrentLinkedQueue<>();

    @Setter
    private Consume<T> consume = new OrderConsume<>(false);

    public PushConsumer() {
        consume();
    }

    @Override
    public void consume(Event<T> event) {
        queue.add(event);
    }

    private void consume() {
        consume.consume(queue);
    }

}
