package org.omni.monitor.toolkit;

import io.micrometer.core.instrument.*;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.omni.monitor.image.ExpireImage;
import org.omni.monitor.image.impl.CountImage;
import org.omni.monitor.image.impl.GaugeImage;
import org.omni.monitor.image.impl.RunnableImage;
import org.omni.toolkit.sug.Sugars;
import org.omni.toolkit.vir.Virs;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Xieningjun
 */
public class Images {

    private static final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    private static final AtomicLong meterCounter = new AtomicLong(0);

    // todo 要支持可配
    private static final long clearLimit = 1;

    // todo 要支持可配
    private static final long maxLimit = 10;

    static {
        registry.config().onMeterAdded(meter -> meterCounter.incrementAndGet());
        registry.config().onMeterRemoved(meter -> meterCounter.decrementAndGet());
    }

    public static void composite() {
        Metrics.addRegistry(registry);
    }

    public static GaugeImage ofGauge(long initial, String key, String ... tags) {
        Sugars.if$catch(meterCounter.get() >= maxLimit, new IllegalStateException("Registry is full."));
        var ref = new AtomicLong(initial);
        var meter = Gauge.builder(key, ref, AtomicLong::get)
                .tags(tags)
                .register(registry);
        var image = new GaugeImage(ref);
        hang(image, meter);
        return image;
    }

    public static CountImage ofCount(String key, String ... tags) {
        Sugars.if$catch(meterCounter.get() >= maxLimit, new IllegalStateException("Registry is full."));
        var meter = Counter.builder(key)
                .tags(tags)
                .register(registry);
        var image = new CountImage(meter);
        hang(image, meter);
        return image;
    }

    public static RunnableImage ofRunnable(Runnable runnable, String key, String ... tags) {
        Sugars.if$catch(meterCounter.get() >= maxLimit, new IllegalStateException("Registry is full."));
        var meter = Timer.builder(key)
                .tags(tags)
                .publishPercentileHistogram(false)
                .publishPercentiles()
                .register(registry);
        var image = new RunnableImage(() -> meter.record(runnable));
        hang(image, meter);
        return image;
    }

    private static void hang(ExpireImage<?> image, Meter meter) {
        Virs.hang(
                () -> registry.remove(meter.getId()),
                () -> meterCounter.get() > clearLimit && !image.positive()
        );
    }

    public static String snapshot() {
        return registry.scrape();
    }

}
