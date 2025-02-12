import com.sun.net.httpserver.HttpExchange;
import org.junit.Test;
import org.omni.venti.FlowChain;
import org.omni.venti.dto.ForwarderContext;
import org.omni.venti.dto.ReceiverContext;
import org.omni.venti.dto.ReqHandlerContext;
import org.omni.venti.dto.RespHandlerContext;
import org.omni.venti.node.*;

/**
 * @author Xieningjun
 * @date 2025/2/12 18:29
 * @description
 */
public class FlowTest {

    @Test
    public void flow() {
        var chain = new FlowChain();
        chain.start(new ReceiverNode() {
            @Override
            public void doProcess(HttpExchange exchange, ReceiverContext receiverContext) {
                System.out.println(receiverContext);
            }
        }).next(new ReqHandlerNode() {
            @Override
            public void doProcess(HttpExchange exchange, ReceiverContext receiverContext, ReqHandlerContext reqHandlerContext) {
                System.out.println(receiverContext);
                System.out.println(reqHandlerContext);
            }
        }).next(new ForwarderNode() {
            @Override
            public void doProcess(HttpExchange exchange, ReqHandlerContext reqHandlerContext, ForwarderContext forwarderContext) {
                System.out.println(reqHandlerContext);
                System.out.println(forwarderContext);
            }
        }).next(new RespHandlerNode() {
            @Override
            public void doProcess(HttpExchange exchange, ForwarderContext forwarderContext, RespHandlerContext respHandlerContext) {
                System.out.println(forwarderContext);
                System.out.println(respHandlerContext);
            }
        }).end(new ResponderNode() {
            @Override
            public void doProcess(HttpExchange exchange, RespHandlerContext respHandlerContext) {
                System.out.println(respHandlerContext);
            }
        });
        chain.process(null, null);
    }

}
