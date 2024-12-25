package org.omni.toolkit.design.mq.consumer;

import org.omni.toolkit.design.event.Event;
import org.omni.toolkit.design.mq.consumer.consume.Consume;
import org.omni.toolkit.vir.Virs;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Xieningjun
 * @date 2024/12/25 11:03
 * @description
 */
public class PullConsumer<T> implements Consumer<T> {

    private final Queue<Event<T>> queue = new ConcurrentLinkedQueue<>();

    private Consume<T> consume;

    private Virs.LoopFuture future;

    // todo 很丑 想想还有啥办法
    private final List<Queue<Event<T>>> topicQueueList = new CopyOnWriteArrayList<>();

    @Override
    public void addToConsumeQueue(Event<T> event) {
        queue.add(event);
    }

    @Override
    public void setConsume(Consume<T> consume) {
        this.consume = consume;
        consume();
    }

    public void addTopicQueue(Queue<Event<T>> queue) {
        if (!topicQueueList.contains(queue)) {
            topicQueueList.add(queue);
        }
    }

    // todo 规范一下命名
    public void pull() {
        for (var queue : topicQueueList) {
            var event = queue.poll();
            if (event != null) {
                this.queue.add(event);
                break;
            }
        }
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

}
