package org.omni.toolkit.design.mq.consumer;

import org.omni.toolkit.design.mq.consumer.consume.Consume;
import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.topic.MsgQueueClient;

/**
 * @author Xieningjun
 * @date 2024/11/11 15:47
 * @description
 */
public interface Consumer<T> extends MsgQueueClient {

    void consume(Event<T> event);

    void setConsume(Consume<T> consume);

}
