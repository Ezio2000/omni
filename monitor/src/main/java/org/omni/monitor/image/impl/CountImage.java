package org.omni.monitor.image.impl;

import io.micrometer.core.instrument.Counter;
import org.omni.monitor.image.ExpireImage;

/**
 * @author Xieningjun
 */
public class CountImage extends ExpireImage<Long> {

    private final Counter counter;

    public CountImage(Counter counter) {
        this.counter = counter;
    }

    @Override
    public Long ref() {
        return (long) counter.count();
    }

    public void count() {
        count(1);
    }

    public void count(long plus) {
        uTime();
        counter.increment(plus);
    }

}
