package org.omni.http.resp;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author Xieningjun
 */
public class GenericBodyHandler<T> implements HttpResponse.BodyHandler<T> {

    private final Type type;

    public GenericBodyHandler(Type type) {
        this.type = type;
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {
        // 创建一个 BodySubscriber，处理响应体并转换为字节数组
        var upstream = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
        // 处理响应字节数组并转换为 T 类型对象
        return HttpResponse.BodySubscribers.mapping(upstream, this::convert);
    }

    // 将 JSON 字符串转换为 T 类型对象
    private T convert(String body) {
        var gson = new Gson();
        // todo 会有报格式转换错误
        return gson.fromJson(body, type);
    }

}