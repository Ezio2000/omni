package org.omni.http.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.Executor;

/**
 * @author Xieningjun
 * @date 2025/1/22 17:42
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FluxMethod {

    String name();

    int duration();

}
