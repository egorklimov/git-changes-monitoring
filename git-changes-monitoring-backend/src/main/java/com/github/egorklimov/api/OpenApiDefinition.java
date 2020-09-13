package com.github.egorklimov.api;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        tags = {
                @Tag(name="repository", description="Operations related to git repositories.")
        },
        info = @Info(
                title="Git Changes Monitoring",
                version = "0.1.1"
        )
)
public class OpenApiDefinition extends Application {
}