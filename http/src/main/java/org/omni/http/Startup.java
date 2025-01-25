package org.omni.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * @author Xieningjun
 */
@SpringBootApplication
public class Startup {

    @Component
    public static class InnerConfig {}

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
    }

}
