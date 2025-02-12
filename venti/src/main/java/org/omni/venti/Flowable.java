package org.omni.venti;

import com.sun.net.httpserver.HttpExchange;
import org.omni.venti.dto.FlowContext;

/**
 * @author Xieningjun
 * @date 2025/2/12 16:02
 * @description
 */
public interface Flowable {
    FlowContext process(HttpExchange exchange, FlowContext context);
}
