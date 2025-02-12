package org.omni.venti.dto;

import com.sun.net.httpserver.HttpExchange;

/**
 * @author Xieningjun
 * @date 2025/2/12 16:37
 * @description
 */
public abstract class FlowContext {

    private HttpExchange exchange;

    protected FlowContext(HttpExchange exchange) {
        this.exchange = exchange;
    }

//    private ReceiverContext receiverContext;
//
//    private ReqHandlerContext reqHandlerContext;
//
//    private ForwarderContext forwarderContext;
//
//    private RespHandlerContext respHandlerContext;
//
//    private ResponderContext responderContext;

}
