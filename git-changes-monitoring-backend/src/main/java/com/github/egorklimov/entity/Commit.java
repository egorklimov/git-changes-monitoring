package com.github.egorklimov.entity;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.commit.CommitDTO;
import com.github.egorklimov.data.repository.contributor.Contributor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commit {

    private String hash;
    private Date commitDate;
    private Date authorDate;
    private Branch branch;
    private Contributor committer;
    private Contributor author;
    private String message;

    public CommitDTO asDto() {
        return new CommitDTO(
                hash,
                commitDate,
                authorDate,
                branch.getId(),
                committer.getMail(),
                author.getMail(),
                message
        );
    }
}