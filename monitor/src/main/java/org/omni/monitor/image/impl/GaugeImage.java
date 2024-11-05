package org.omni.monitor.image.impl;

import org.omni.monitor.image.ExpireImage;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Xieningjun
 */
public class GaugeImage extends ExpireImage<Long> {

    private final AtomicLong ref;

    public GaugeImage(AtomicLong ref) {
        this.ref = ref;
    }

    @Override
    public Long getRef() {
        return ref.get();
    }

    public void gauge(Long uRef) {
        upTime();
        ref.set(uRef);
    }

}
