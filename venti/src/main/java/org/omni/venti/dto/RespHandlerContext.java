package org.omni.venti.dto;

import com.sun.net.httpserver.HttpExchange;

/**
 * @author Xieningjun
 * @date 2025/2/12 16:51
 * @description
 */
public class RespHandlerContext extends FlowContext {
    public RespHandlerContext(HttpExchange exchange) {
        super(exchange);
    }
}
