package org.omni.block.mine;

import org.omni.block.block.Block;
import org.omni.toolkit.design.event.Event;
import org.omni.toolkit.design.event.EventListener;
import org.omni.toolkit.design.mq.consumer.PushConsumer;
import org.omni.toolkit.design.mq.consumer.consume.OrderConsume;

/**
 * @author Xieningjun
 * @date 2024/12/24 16:31
 */
public class Miner<T> extends PushConsumer<T> {

    public Miner() {
        var consume = new OrderConsume<T>(true);
        var listener = new EventListener<T>() {
            @Override
            public void listen(Event<T> event) {
                var block = (Block<T>) event;
                var hash = block.mine();
                block.setHash(hash);
            }
        };
        consume.setListener(listener);
        super.setConsume(consume);
    }

}
