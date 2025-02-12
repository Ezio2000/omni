package org.omni.venti.dto;

import com.sun.net.httpserver.HttpExchange;

/**
 * @author Xieningjun
 * @date 2025/2/12 16:50
 * @description
 */
public class ReqHandlerContext extends FlowContext {
    public ReqHandlerContext(HttpExchange exchange) {
        super(exchange);
    }
}
