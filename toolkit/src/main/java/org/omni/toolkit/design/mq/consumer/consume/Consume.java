package org.omni.toolkit.design.mq.consumer.consume;

import org.omni.toolkit.design.event.Event;
import org.omni.toolkit.vir.Virs;

import java.util.Queue;

/**
 * @author Xieningjun
 * @date 2024/11/12 12:24
 * @description
 */
public interface Consume<T> {

    Virs.LoopFuture consume(Queue<Event<T>> queue);

}
