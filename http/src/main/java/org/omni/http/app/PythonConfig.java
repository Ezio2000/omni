package org.omni.http.app;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Xieningjun
 * @date 2025/1/20 1:45
 * @description
 */
@Data
@Configuration
@PropertySource("classpath:python.yml")
public class PythonConfig {

    private String version;

}
