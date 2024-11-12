package org.omni.toolkit.design.mq.consumer.consume;

import lombok.Setter;
import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.event.EventListener;
import org.omni.toolkit.sug.Sugars;
import org.omni.toolkit.vir.Virs;

import java.util.Queue;

/**
 * @author Xieningjun
 * @date 2024/11/12 12:25
 * @description
 */
public class OrderConsume<T> implements Consume<T> {

    private final boolean isOrder;

    @Setter
    private EventListener<T> listener;

    public OrderConsume(boolean isOrder) {
        this.isOrder = isOrder;
    }

    @Override
    public void consume(Queue<Event<T>> queue) {
        Sugars.$ifNull$throw(listener, new IllegalAccessError("Listener can't be null."));
        Virs.loop(() -> {
            var event = queue.poll();
            if (event != null) listener.listen(event);
        }, -1, 1, isOrder);
    }

}
