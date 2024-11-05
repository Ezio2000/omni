package org.omni.toolkit.empty;

import java.util.Objects;

/**
 * @author Xieningjun
 */
public class EmptyObj {

    private static final EmptyObj emptyObj = new EmptyObj();

    private EmptyObj() {}

    public static EmptyObj of() {
        return Objects.requireNonNullElseGet(emptyObj, EmptyObj::new);
    }

}
