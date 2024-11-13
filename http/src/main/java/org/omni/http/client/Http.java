package org.omni.http.client;

import com.google.gson.Gson;
import org.omni.http.resp.GenericBodyHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * @author Xieningjun
 * @date 2024/11/13 16:23
 * @description
 */
public class Http {

    private final HttpConfigThreadLocal config = new HttpConfigThreadLocal();

    public Http() {}

    public Http(long connTimeout, long readTimeout) {
        config.setConnTimeout(connTimeout);
        config.setReadTimeout(readTimeout);
    }

    public void async() {

    }

    // 可以想把client.send(req, new GenericBodyHandler<>(type));封装成runnable返回，再让人手动执行call();
    public <T> HttpCaller<T> post(String url, Object body) {
        var gson = new Gson();
        var json = gson.toJson(body);
        var client = HttpClient.newBuilder()
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .connectTimeout(Duration.ofMillis(config.getConnTimeout()))
                .build();
        var req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofMillis(config.getReadTimeout()))
                .build();
        return new HttpCaller<>((type) -> {
            try {
                HttpResponse<T> resp = client.send(req, new GenericBodyHandler<>(type));
                return resp.body();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setCurConnTimeout(long connTimeout) {
        config.setConnTimeout(connTimeout);
    }

    public void setCurReqTimout(long readTimeout) {
        config.setReadTimeout(readTimeout);
    }

    public static class HttpCaller<T> {
        private final Function<Type, T> func;
        public HttpCaller(Function<Type, T> func) {
            this.func = func;
        }
        public T call(Type type) {
            return func.apply(type);
        }
    }

}
