package com.github.egorklimov.api;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        tags = {
                @Tag(name="repository", description="Operations related to git repositories."),
                @Tag(name="commit", description="Operations related to git commits."),
                @Tag(name="tag", description="Operations related to organizations.")
        },
        info = @Info(
                title="git-changes-monitoring",
                version = "v0.2.0"
        )
)
public class OpenApiDefinition extends Application {
}