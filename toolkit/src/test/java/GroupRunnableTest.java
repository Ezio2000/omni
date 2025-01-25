import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.omni.toolkit.vir.GroupRunnable;
import org.omni.toolkit.vir.Virs;

/**
 * @author Xieningjun
 * @date 2025/1/25 10:40
 * @description
 */
@Slf4j
public class GroupRunnableTest {

    @Test
    public void test() {
        var group = new GroupRunnable(
                () -> {
                    Virs.sleep(1000);
                    log.info("1000ms完成");
                },
                () -> {
                    Virs.sleep(2000);
                    log.info("2000ms完成");
                },
                () -> {
                    Virs.sleep(3000);
                    log.info("3000ms完成");
                }
        );
        group.run(false);
        Virs.keepalive();
    }

}
