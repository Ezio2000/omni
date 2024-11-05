package org.omni.toolkit.empty;

import java.util.Objects;

/**
 * @author Xieningjun
 */
public class EmptyRunnable implements Runnable {

    private static final EmptyRunnable emptyRunnable = new EmptyRunnable();

    private EmptyRunnable() {}

    public static EmptyRunnable of() {
        return Objects.requireNonNullElseGet(emptyRunnable, EmptyRunnable::new);
    }

    @Override
    public void run() {
//        throw new IllegalStateException("Runnable is empty.");
    }

}
