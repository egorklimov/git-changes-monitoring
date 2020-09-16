package com.github.egorklimov.api.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitRepositoryCloneRequest {
    private String url;
}
