package org.omni.toolkit.vir;

import org.omni.toolkit.sug.Sugars;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Xieningjun
 */
class LoopRunnable implements Runnable {

    private final long loop;

    private final AtomicLong innerCounter = new AtomicLong(0);

    private final AtomicLong outerCounter = new AtomicLong(0);

    private final AtomicBoolean loopFinished = new AtomicBoolean(false);

    private final Runnable delegate;

    LoopRunnable(Runnable delegate, long loop) {
        this.delegate = delegate;
        this.loop = loop;
    }

    // todo 有没有风险？
    @Override
    public void run() {
        if (innerCounter.get() < loop || loop == -1) {
            innerCounter.incrementAndGet();
            delegate.run();
            outerCounter.incrementAndGet();
        } else {
            Sugars.$if$throw(
                    outerCounter.get() >= loop && loopFinished.compareAndSet(false, true),
                    new LoopException(loop, outerCounter.get())
            );
        }
    }

    static class LoopException extends RuntimeException {

        public final long loop;

        public final long actual;

        public LoopException(long loop, long actual) {
            this.loop = loop;
            this.actual = actual;
        }

    }

}
