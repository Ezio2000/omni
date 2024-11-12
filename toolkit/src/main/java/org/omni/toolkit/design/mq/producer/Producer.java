package org.omni.toolkit.design.mq.producer;

import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.topic.MsgQueueClient;

/**
 * @author Xieningjun
 * @date 2024/11/11 16:11
 * @description
 */
public interface Producer<T> extends MsgQueueClient {

    void produce(Event<T> event);

    Event<T> getEvent();

}
