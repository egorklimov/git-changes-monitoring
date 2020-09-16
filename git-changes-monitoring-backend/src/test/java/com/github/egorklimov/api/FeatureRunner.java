package com.github.egorklimov.api;

import com.github.egorklimov.resource.TestResourceLifecycleManager;
import com.intuit.karate.junit5.Karate;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
@QuarkusTest
@QuarkusTestResource(TestResourceLifecycleManager.class)
public class FeatureRunner {

    @SneakyThrows
    @Karate.Test
    Karate test_add_github_scenario() {
        Karate karate = Karate.run("features/add-github-repository")
                .relativeTo(getClass());
        TimeUnit.MINUTES.sleep(10);
        return karate;
    }
}