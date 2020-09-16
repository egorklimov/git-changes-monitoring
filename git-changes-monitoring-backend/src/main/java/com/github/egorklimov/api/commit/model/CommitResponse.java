package com.github.egorklimov.api.commit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommitResponse {
    private String hash;
    private String commitDate;
    private String authorDate;
    private String committer;
    private String author;
    private String message;
    private DateDetails commitDateDetails;
    private DateDetails authorDateDetails;
}
