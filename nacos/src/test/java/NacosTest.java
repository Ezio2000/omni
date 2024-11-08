import com.alibaba.nacos.api.exception.NacosException;
import org.junit.Test;
import org.omni.nacos.client.Nacos;
import org.omni.nacos.manager.NacosValueManager;
import org.omni.toolkit.vir.Virs;

/**
 * @author Xieningjun
 */
public class NacosTest {

    @Test
    public void register() throws NacosException, InterruptedException {
        var nacosValueManager = new NacosValueManager();
        nacosValueManager.insert("omni.yml", "omni");
        nacosValueManager.insert("omni.yml", "a.b.c");
        var discovery = new Nacos(
                "nacos-dev.ruqimobility.com:80",
                "namespace-dev",
                "cluster-dev",
                "travel-dev",
                "omni",
                "127.0.0.1",
                8003
        );
        discovery.setNacosValueManager(nacosValueManager);
//        discovery.meta("version", "1.0");
        discovery.register();
        discovery.subscribe("omni.yml");
        discovery.meta("version", "2.0");
        Thread.sleep(1000);
        System.out.println(nacosValueManager.query("omni.yml"));
        Virs.keepalive();
    }

    @Test
    public void discover() throws NacosException {
        var discovery = new Nacos(
                "nacos-dev.ruqimobility.com:80",
                "namespace-dev",
                "cluster-dev",
                "travel-dev",
                "omni2",
                "127.0.0.1",
                8003
        );
        discovery.register();
        System.out.println(discovery.discover("omni"));
    }

}
