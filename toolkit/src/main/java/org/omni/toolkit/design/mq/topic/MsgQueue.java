package org.omni.toolkit.design.mq.topic;

import org.omni.toolkit.design.mq.consumer.Consumer;
import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.producer.Producer;

/**
 * @author Xieningjun
 * @date 2024/11/11 15:48
 * @description
 */
public interface MsgQueue<T> {

    void subscribe(Consumer<T> consumer);

    void unsubscribe(Consumer<T> consumer);

    void subscribe(Producer<T> producer);

    void unsubscribe(Producer<T> producer);

    void submit(Event<T> event);

    void submit(Event<T> event, int index);

    void push();

    Event<T> pull();

}
