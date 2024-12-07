import org.junit.Test;
import org.omni.grafana.client.Auth;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Arrays;

/**
 * @author Xieningjun
 * @date 2024/11/13 13:57
 * @description
 */
public class AuthTest {

    @Test
    public void grafanaAuth() throws IOException, InterruptedException {
        var username = "ruqi_jiagou_team";
        var password = "l6DcamPFjRLwgg1P";
        var body = "{\"user\":\"%s\", \"password\":\"%s\"}".formatted(username, password);
        var req = HttpRequest.newBuilder()
                .uri(URI.create("https://vmselect-pro.ruqimobility.com/login"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        var auth = new Auth(req);
        var client = auth.httpStatusAuth(Arrays.asList(200));
        System.out.println(client);
    }

}
