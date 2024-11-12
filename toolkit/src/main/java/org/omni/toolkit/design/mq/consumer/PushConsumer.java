package org.omni.toolkit.design.mq.consumer;

import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.consumer.consume.Consume;
import org.omni.toolkit.vir.Virs;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Xieningjun
 * @date 2024/11/12 11:16
 */
public class PushConsumer<T> implements Consumer<T> {

    private final Queue<Event<T>> queue = new ConcurrentLinkedQueue<>();

    private Consume<T> consume;

    private Virs.LoopFuture future;

    @Override
    public void consume(Event<T> event) {
        queue.add(event);
    }

    private void consume() {
        // todo 改简洁
        if (consume != null) {
            if (future != null) {
                future.close();
            }
            future = consume.consume(queue);
        }
    }

    public void setConsume(Consume<T> consume) {
        this.consume = consume;
        consume();
    }

}
