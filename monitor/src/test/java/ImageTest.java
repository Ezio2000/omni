import org.omni.monitor.toolkit.Images;
import org.omni.toolkit.vir.Virs;

import java.time.Duration;

/**
 * @author Xieningjun
 */
public class ImageTest {

    public static void main(String[] args) throws InterruptedException {
        Images.composite();
        for (int i = 0; i < 100; i++) {
            try {
                var image = Images.ofGauge(0, "http", "a", String.valueOf(i));
                image.setETime(Duration.ofSeconds(10));
                if (i == 0) {
                    Virs.loop(() -> image.gauge(System.currentTimeMillis()), 100, 500);
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
