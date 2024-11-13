package org.omni.http.client;

/**
 * @author Xieningjun
 * @date 2024/11/13 17:07
 * @description
 */
public class HttpConfigThreadLocal extends ThreadLocal<HttpConfig> {

    public HttpConfigThreadLocal() {
        initialValue();
    }

    public long getConnTimeout() {
        return get().getConnTimeout();
    }

    public void setConnTimeout(long timeout) {
        get().setConnTimeout(timeout);
    }

    public long getReadTimeout() {
        return get().getReadTimeout();
    }

    public void setReadTimeout(long timeout) {
        get().setReadTimeout(timeout);
    }

    @Override
    protected HttpConfig initialValue() {
        return new HttpConfig();
    }

}
