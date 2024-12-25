import org.junit.Test;
import org.omni.http.client.Http;

/**
 * @author Xieningjun
 * @date 2024/11/20 11:38
 * @description
 */
public class HttpTest {

    @Test
    public void post() {
        var http = new Http(1000L, 1000L);
        var caller = http.post("https://www.baidu.com", null);
        var str = caller.call(Object.class);
        System.out.println(str);
    }

}
