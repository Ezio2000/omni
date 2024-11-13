package org.omni.grafana.client;

import org.omni.http.resp.AdaptiveBodyHandler;
import org.omni.http.resp.GenericBodyHandler;
import org.omni.toolkit.vir.Virs;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author Xieningjun
 * @date 2024/11/13 13:47
 * @description
 */
public class Auth {

    private final HttpRequest loginReq;

    private final HttpClient client;

    public Auth(HttpRequest loginReq) {
        this.loginReq = loginReq;
        var cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = HttpClient
                .newBuilder()
                .cookieHandler(cookieManager)
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .build();
    }

    public synchronized HttpClient httpStatusAuth(List<Integer> validStatus) throws IOException, InterruptedException {
        // 发送请求并获取响应
        var resp = client.send(loginReq, HttpResponse.BodyHandlers.ofString());
        var status = resp.statusCode();
        if (validStatus.contains(status)) {
            return client;
        }
        // todo 封装
        throw new RuntimeException("Invalid status code " + status);
    }

}
