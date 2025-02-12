package org.omni.venti.node;

import com.sun.net.httpserver.HttpExchange;
import org.omni.venti.Flowable;
import org.omni.venti.dto.FlowContext;
import org.omni.venti.dto.ForwarderContext;
import org.omni.venti.dto.RespHandlerContext;

/**
 * @author Xieningjun
 * @date 2025/2/12 17:07
 * @description
 */
public abstract class RespHandlerNode implements Flowable {

    @Override
    public final RespHandlerContext process(HttpExchange exchange, FlowContext context) {
        var respHandlerContext = new RespHandlerContext(exchange);
        doProcess(exchange, (ForwarderContext) context, respHandlerContext);
        return respHandlerContext;
    }

    public abstract void doProcess(HttpExchange exchange, ForwarderContext forwarderContext, RespHandlerContext respHandlerContext);

}
