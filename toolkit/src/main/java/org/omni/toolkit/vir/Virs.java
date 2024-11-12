package org.omni.toolkit.vir;

import org.omni.toolkit.runnable.LoopRunnable;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Xieningjun
 */
public class Virs {

    // todo 如何设置为可调大小？
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2 /* 改成初始化 */, Thread.ofVirtual().factory());

    private static final Map<Future<?>, AtomicBoolean> cancelMap = new ConcurrentHashMap<>();

    static  {
        /* 手动结束定时任务 */
        scheduler.scheduleAtFixedRate(() -> {
            for (var entry : cancelMap.entrySet()) {
                var future = entry.getKey();
                var cancel = entry.getValue();
                if (cancel.get()) {
                    // todo true or false？
                    future.cancel(true);
                    cancelMap.remove(future);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public static Thread one(Runnable runnable) {
        return Thread.ofVirtual().start(runnable);
    }

    public static LoopFuture loop(Runnable runnable, long loop, long period, boolean order) {
        var loopRunnable = new LoopRunnable(runnable, loop);
        var cancel = new AtomicBoolean(false);
        // !order让其在一个任务开始后，即可马上开始下一个任务
        if (order) {
            var future = scheduler.scheduleAtFixedRate(() -> {
                try {
                    loopRunnable.run();
                } catch (LoopRunnable.LoopException e) {
                    cancel.set(true);
                }
            }, 0, period, TimeUnit.MILLISECONDS /* 也就是说 目前只支持到1000rps 如果要支持更高rps 需要改nano */);
            cancelMap.put(future, cancel);
        } else {
            var future = scheduler.scheduleAtFixedRate(() -> {
                one(() -> {
                    try {
                        loopRunnable.run();
                    } catch (LoopRunnable.LoopException e) {
                        cancel.set(true);
                    }
                });
            }, 0, period, TimeUnit.MILLISECONDS /* 也就是说 目前只支持到1000rps 如果要支持更高rps 需要改nano */);
            cancelMap.put(future, cancel);
        }
        return new LoopFuture(cancel);
    }

    public static void after(Runnable runnable, long after) {
        Virs.one(() -> {
            Virs.sleep(after);
            runnable.run();
        });
    }

    public static void hang(Runnable runnable, Condition condition) {
        one(() -> {
            try {
                while (!condition.isOk()) {
                    LockSupport.parkNanos(500L * 1_000_000);
                }
            } finally {
                runnable.run();
            }
        });
    }

    public static void hang(Runnable runnable, Thread thread) {
        one(() -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                runnable.run();
            }
        });
    }

    public static void hang(Runnable runnable, Future<?> future) {
        one(() -> {
            try {
                future.get();
            // todo 这里区分开
            } catch (ExecutionException | InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (CancellationException ignored) {
                /* 任务已取消 可以执行下一步 */
            } finally {
                runnable.run();
            }
        });
    }

    public static void sleep(long millis) {
        LockSupport.parkNanos(millis * 1_000_000);
    }

    public static void keepalive() {
        while (true) LockSupport.parkNanos(10000L * 1_000_000);
    }

    public static class LoopFuture implements Closeable {
        private final AtomicBoolean cancel;
        public LoopFuture(AtomicBoolean cancel) {
            this.cancel = cancel;
        }
        @Override
        public void close() {
            cancel.set(true);
        }
    }

}
