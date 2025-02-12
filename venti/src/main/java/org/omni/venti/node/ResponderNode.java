package org.omni.venti.node;

import com.sun.net.httpserver.HttpExchange;
import org.omni.venti.Flowable;
import org.omni.venti.signal.EndSignal;
import org.omni.venti.dto.FlowContext;
import org.omni.venti.dto.RespHandlerContext;

/**
 * @author Xieningjun
 * @date 2025/2/12 17:07
 * @description
 */
public abstract class ResponderNode extends EndSignal implements Flowable {

    @Override
    public final FlowContext process(HttpExchange exchange, FlowContext context) {
        doProcess(exchange, (RespHandlerContext) context);
        return null;
    }

    public abstract void doProcess(HttpExchange exchange, RespHandlerContext respHandlerContext);

}
