package com.github.egorklimov.git;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "repository")
public class RepositoryLocalStorageConfiguration {

    /**
     * Path to store all repos.
     */
    private String storage;
}
