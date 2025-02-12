package org.omni.venti.node;

import com.sun.net.httpserver.HttpExchange;
import org.omni.venti.Flowable;
import org.omni.venti.dto.FlowContext;
import org.omni.venti.dto.ReceiverContext;
import org.omni.venti.signal.StartSignal;

/**
 * @author Xieningjun
 * @date 2025/2/12 17:07
 * @description
 */
public abstract class ReceiverNode extends StartSignal implements Flowable {

    @Override
    public final ReceiverContext process(HttpExchange exchange, FlowContext context) {
        var receiverContext = new ReceiverContext(exchange);
        doProcess(exchange, receiverContext);
        return receiverContext;
    }

    public abstract void doProcess(HttpExchange exchange, ReceiverContext receiverContext);

}
