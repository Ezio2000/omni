package org.omni.venti.node;

import com.sun.net.httpserver.HttpExchange;
import org.omni.venti.Flowable;
import org.omni.venti.dto.FlowContext;
import org.omni.venti.dto.ForwarderContext;
import org.omni.venti.dto.ReqHandlerContext;

/**
 * @author Xieningjun
 * @date 2025/2/12 17:07
 * @description
 */
public abstract class ForwarderNode implements Flowable {

    @Override
    public final ForwarderContext process(HttpExchange exchange, FlowContext context) {
        var forwarderContext = new ForwarderContext(exchange);
        if (context instanceof ReqHandlerContext) {
            doProcess(exchange, (ReqHandlerContext) context, forwarderContext);
        }
        return forwarderContext;
    }

    public abstract void doProcess(HttpExchange exchange, ReqHandlerContext reqHandlerContext, ForwarderContext forwarderContext);

}
