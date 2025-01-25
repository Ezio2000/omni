package org.omni.toolkit.vir;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xieningjun
 */
@Slf4j
public class GroupRunnable implements Runnable {

    private final List<Runnable> group;

    private final AtomicBoolean isBlock = new AtomicBoolean(true);

    public GroupRunnable(Runnable... group) {
        this.group = Arrays.asList(group);
    }

    @Override
    public void run() {
        if (isBlock.get()) {
            blockRun();
        } else {
            unblockRun();
        }
    }

    public void run(boolean isBlock) {
        this.isBlock.set(isBlock);
        run();
    }

    private synchronized void blockRun() {
        var latch = new CountDownLatch(group.size());
        for (var runnable : group) {
            Virs.one(() -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }
        new LatchRunnable(latch).run();
    }

    private synchronized void unblockRun() {
        var latch = new CountDownLatch(group.size());
        Virs.one(new LatchRunnable(latch));
        for (var runnable : group) {
            Virs.one(() -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }
    }

    private record LatchRunnable(CountDownLatch latch) implements Runnable {
        @Override
            public void run() {
                try {
                    latch.await();
                    log.info("GroupRunnable finish.");
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

}
