package com.github.egorklimov.engine.executor;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "executor")
public class ExecutorConfiguration {
    private int corePoolSize;
    private int additionalPoolSize;
}
