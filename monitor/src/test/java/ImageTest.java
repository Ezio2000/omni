import org.junit.Test;
import org.omni.monitor.toolkit.Images;
import org.omni.toolkit.vir.Virs;

import java.time.Duration;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Xieningjun
 */
public class ImageTest {

    @Test
    public void gauge() throws InterruptedException {
        Images.composite();
        for (int i = 0; i < 100; i++) {
            try {
                var image = Images.ofGauge(0, "http", "a", String.valueOf(i));
                image.setETime(Duration.ofSeconds(10));
                if (i == 0) {
                    Virs.loop(() -> image.gauge(System.currentTimeMillis()), 100, 500, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println(Images.snapshot());
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void count() throws InterruptedException {
        Images.composite();
        for (int i = 0; i < 100; i++) {
            try {
                var image = Images.ofCount("http", "a", String.valueOf(i));
                image.setETime(Duration.ofSeconds(10));
                if (i == 0) {
                    Virs.loop(image::count, 100, 500, false);
                }
                if (i == 1) {
                    Virs.loop(() -> image.count(2), 100, 500, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println(Images.snapshot());
                Thread.sleep(500);
            }
        }
    }

    @Test
    public void runnable() throws InterruptedException {
        Images.composite();
        for (int i = 0; i < 100; i++) {
            try {
                var image = Images.ofRunnable(() -> {
                    System.out.println(Thread.currentThread());
                    LockSupport.parkNanos(1000L * 1_000_000);
                }, "http", "a", String.valueOf(i));
                image.setETime(Duration.ofSeconds(10));
                if (i == 0) {
                    Virs.loop(image::runnable, 100, 500, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println(Images.snapshot());
                Thread.sleep(500);
            }
        }
    }

}
