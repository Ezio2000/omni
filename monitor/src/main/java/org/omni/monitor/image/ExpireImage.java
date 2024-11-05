package org.omni.monitor.image;

import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Xieningjun
 */
public abstract class ExpireImage<T> implements Image<T> {

    private Instant uTime = Instant.now();

    // todo 启动可配
    @Setter
    private Duration eTime = Duration.ofMinutes(1);

    @Override
    public abstract T getRef();

    protected final void upTime() {
        // todo 能去掉before吗
        if (uTime.isBefore(Instant.now())) {
            uTime = Instant.now();
        }
    }

    @Override
    public final boolean isPositive() {
        return Duration.between(uTime, Instant.now()).compareTo(eTime) < 0;
    }

}
