package org.omni.toolkit.sug;

/**
 * @author Xieningjun
 */
public class Sugars {

    public static <E extends Throwable> void $if$throw(boolean condition, E e) throws E {
        if (condition) {
            throw e;
        }
    }

    public static <E extends Throwable> void $ifNull$throw(Object o, E e) throws E {
        $if$throw(o == null, e);
    }

    public static <E extends Throwable> void $ifNotNull$throw(Object o, E e) throws E {
        $if$throw(o != null, e);
    }

}
