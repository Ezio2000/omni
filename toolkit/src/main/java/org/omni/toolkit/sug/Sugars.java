package org.omni.toolkit.sug;

/**
 * @author Xieningjun
 */
public class Sugars {

    public static <E extends Throwable> void if$catch(boolean condition, E e) throws E {
        if (condition) {
            throw e;
        }
    }

}
