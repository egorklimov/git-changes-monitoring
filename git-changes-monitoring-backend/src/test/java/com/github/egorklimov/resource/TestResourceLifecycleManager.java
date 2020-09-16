package com.github.egorklimov.resource;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Map;

@Slf4j
public class TestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private static final String DATABASE_NAME = "TEST";
    private static final String USERNAME = "TEST";
    private static final String PASSWORD = "TEST";

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>()
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    @Override
    public Map<String, String> start() {
        try {
            postgres.start();
            return ImmutableMap.of(
                "quarkus.datasource.jdbc.url", postgres.getJdbcUrl(),
                "quarkus.datasource.username", USERNAME,
                "quarkus.datasource.password", PASSWORD
            );
        } catch (Exception e) {
            log.error("Failed to start pg container", e);
            Assertions.fail(e.getMessage());
            throw new RuntimeException("Failed to start pg container", e);
        }
    }

    @Override
    public void stop() {
        try {
            postgres.stop();
        } catch (Exception e) {
            log.error("Failed to stop pg container", e);
            Assertions.fail(e.getMessage());
        }
    }
}
