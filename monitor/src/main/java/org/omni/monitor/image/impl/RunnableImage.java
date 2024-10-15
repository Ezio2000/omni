package org.omni.monitor.image.impl;

import org.omni.monitor.image.ExpireImage;

/**
 * @author Xieningjun
 */
public class RunnableImage extends ExpireImage<Runnable> {

    private final Runnable delegate;

    public RunnableImage(Runnable delegate) {
        this.delegate = delegate;
    }

    @Override
    public Runnable ref() {
        return null;
    }

    // todo 是否需要执行时持续关注时间？
    public void runnable() {
        uTime();
        delegate.run();
        uTime();
    }

}
