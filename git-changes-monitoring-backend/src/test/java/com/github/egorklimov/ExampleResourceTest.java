package com.github.egorklimov;

import com.github.egorklimov.resource.TestResourceLifecycleManager;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(TestResourceLifecycleManager.class)
class ExampleResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/api/commit/repo/branch")
          .then()
             .statusCode(404);
    }

}