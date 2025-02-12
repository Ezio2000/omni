package org.omni.venti;

import com.sun.net.httpserver.HttpExchange;
import lombok.Data;
import org.omni.venti.dto.*;
import org.omni.venti.signal.EndSignal;
import org.omni.venti.signal.StartSignal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2025/2/12 17:54
 * @description 
 */
@Data
public class FlowChain implements Flowable {

    private StartSignal startSignal;

    private List<Flowable> flowableList = new ArrayList<>();

    private EndSignal endSignal;

    @Override
    public final FlowContext process(HttpExchange exchange, FlowContext context) {
        FlowContext nextContext = startSignal.process(exchange, null);
        for (var flowable : flowableList) {
            nextContext = flowable.process(exchange, nextContext);
        }
        return endSignal.process(exchange, nextContext);
    }

    public final void process() {
        process(null, null);
    }

    public final FlowChain start(StartSignal startSignal) {
        this.startSignal = startSignal;
        return this;
    }

    public final FlowChain next(Flowable flowable) {
        flowableList.add(flowable);
        return this;
    }

    public final FlowChain end(EndSignal endSignal) {
        this.endSignal = endSignal;
        return this;
    }

}
