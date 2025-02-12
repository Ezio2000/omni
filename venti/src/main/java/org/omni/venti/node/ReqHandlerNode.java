package org.omni.venti.node;

import com.sun.net.httpserver.HttpExchange;
import org.omni.venti.Flowable;
import org.omni.venti.dto.FlowContext;
import org.omni.venti.dto.ReceiverContext;
import org.omni.venti.dto.ReqHandlerContext;

/**
 * @author Xieningjun
 * @date 2025/2/12 17:07
 * @description
 */
public abstract class ReqHandlerNode implements Flowable {

    @Override
    public final ReqHandlerContext process(HttpExchange exchange, FlowContext context) {
        var reqHandlerContext = new ReqHandlerContext(exchange);
        doProcess(exchange, (ReceiverContext) context, reqHandlerContext);
        return reqHandlerContext;
    }

    public abstract void doProcess(HttpExchange exchange, ReceiverContext receiverContext, ReqHandlerContext reqHandlerContext);

}
