package org.omni.venti.dto;

import com.sun.net.httpserver.HttpExchange;

/**
 * @author Xieningjun
 * @date 2025/2/12 16:51
 * @description
 */
public class ResponderContext extends FlowContext {
    public ResponderContext(HttpExchange exchange) {
        super(exchange);
    }
}
