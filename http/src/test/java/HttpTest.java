import org.junit.Test;
import org.omni.http.client.Http;
import org.omni.toolkit.vir.Virs;

import java.util.HashMap;

/**
 * @author Xieningjun
 * @date 2024/11/20 11:38
 * @description
 */
public class HttpTest {

    @Test
    public void get() {
        var http = new Http(1000L, 1000L);
        var caller = http.get("http://10.30.151.207:22400/sendOne", HashMap.newHashMap(2));
        for (int i = 0; i < 1000; i++) {
            Virs.one(() -> {
                var str = caller.call(Object.class);
                System.out.println(str);
            });
        }
        Virs.keepalive();
    }

}
