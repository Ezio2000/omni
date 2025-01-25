package org.omni.http.app;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Xieningjun
 */
@RestController
public class FluxController {

    private final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

    private final Queue<String> queue = new ArrayBlockingQueue<>(10);

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamData() {
        return Flux.interval(Duration.ofSeconds(1))
                .publishOn(Schedulers.fromExecutor(executor))
                .handle((i, sink) -> {
                    var val = queue.poll();
                    sink.next(Objects.requireNonNullElse(val, "default"));
                });
    }

    @GetMapping("/add")
    public Mono<Void> addToQueue(@RequestParam(value = "value") String value) {
        return Mono.fromRunnable(() -> queue.offer(value));
    }

}
