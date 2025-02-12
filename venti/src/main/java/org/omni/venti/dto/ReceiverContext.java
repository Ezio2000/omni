package org.omni.venti.dto;

import com.sun.net.httpserver.HttpExchange;

/**
 * @author Xieningjun
 * @date 2025/2/12 16:49
 * @description
 */
public class ReceiverContext extends FlowContext {
    public ReceiverContext(HttpExchange exchange) {
        super(exchange);
    }
}
