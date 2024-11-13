package org.omni.http.resp;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author Xieningjun
 */
public class AdaptiveBodyHandler implements HttpResponse.BodyHandler {

    @Override
    public HttpResponse.BodySubscriber apply(HttpResponse.ResponseInfo responseInfo) {
        var contentType = responseInfo.headers().firstValue("Content-Type");
        if (contentType.isPresent()) {
            var contentTypeValue = contentType.get();
            if (contentTypeValue.contains("application/json") || contentTypeValue.contains("text")) {
                return HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
            } else if (contentTypeValue.contains("application/octet-stream")) {
                return HttpResponse.BodySubscribers.ofByteArray();
            } else if (contentTypeValue.contains("image") || contentTypeValue.contains("octet-stream")) {
                return HttpResponse.BodySubscribers.ofInputStream();
            }
        }
        return HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
    }

}
