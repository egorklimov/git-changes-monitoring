package com.github.egorklimov.api;

import com.github.egorklimov.resource.TestResourceLifecycleManager;
import com.intuit.karate.junit5.Karate;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(TestResourceLifecycleManager.class)
public class FeatureRunner {

    @Karate.Test
    Karate test_add_github_scenario() {
        return Karate.run("features/add-github-repository")
                .relativeTo(getClass());
    }
}