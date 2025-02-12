package org.omni.venti.dto;

import com.sun.net.httpserver.HttpExchange;

/**
 * @author Xieningjun
 * @date 2025/2/12 16:50
 * @description
 */
public class ForwarderContext extends FlowContext {
    public ForwarderContext(HttpExchange exchange) {
        super(exchange);
    }
}
