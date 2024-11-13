package org.omni.http.client;

import lombok.Data;

/**
 * @author Xieningjun
 * @date 2024/11/13 16:46
 * @description
 */
@Data
public class HttpConfig {

    private long connTimeout = 2000L;

    private long readTimeout = 2000L;

}
