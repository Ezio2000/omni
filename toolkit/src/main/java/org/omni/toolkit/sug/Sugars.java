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

}
