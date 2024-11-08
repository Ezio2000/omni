package org.omni.toolkit.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Xieningjun
 */
public class GenericType<T> {

    private final Type type;

    protected GenericType() {
        this.type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

}
