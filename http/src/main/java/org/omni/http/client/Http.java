package org.omni.http.client;

import com.google.gson.Gson;
import org.omni.http.resp.GenericJsonHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * @author Xieningjun
 * @date 2024/11/13 16:23
 * @description
 */
public class Http {

    private static final HttpConfigThreadLocal config = new HttpConfigThreadLocal();

    private final long initialConnTimeout;

    private final long initialReadTimeout;

    public Http(long connTimeout, long readTimeout) {
        this.initialConnTimeout = connTimeout;
        this.initialReadTimeout = readTimeout;
        config.setConnTimeout(connTimeout);
        config.setReadTimeout(readTimeout);
    }

    // todo 只支持json
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
        resetHttpConfig();
        return getHttpCaller(client, req);
    }

    // todo 只支持json
    public <T> HttpCaller<T> get(String url, Map<String, String> params) {
        var client = HttpClient.newBuilder()
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .connectTimeout(Duration.ofMillis(config.getConnTimeout()))
                .build();
        var paramsList = new ArrayList<String>();
        for (var entry : params.entrySet()) {
            paramsList.add("%s=%s".formatted(entry.getKey(), entry.getValue()));
        }
        var req = HttpRequest.newBuilder()
                .uri(URI.create("%s?%s".formatted(url, String.join("&", paramsList))))
                .GET()
                .timeout(Duration.ofMillis(config.getReadTimeout()))
                .build();
        resetHttpConfig();
        return getHttpCaller(client, req);
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

    private void resetHttpConfig() {
        config.setConnTimeout(initialConnTimeout);
        config.setReadTimeout(initialReadTimeout);
    }

    private <T> HttpCaller<T> getHttpCaller(HttpClient client, HttpRequest req) {
        return new HttpCaller<>((type) -> {
            try {
                HttpResponse<T> resp = client.send(req, new GenericJsonHandler<>(type));
                return resp.body();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
