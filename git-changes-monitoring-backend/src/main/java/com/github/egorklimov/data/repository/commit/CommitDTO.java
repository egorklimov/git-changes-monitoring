package com.github.egorklimov.data.repository.commit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "commit")
@AllArgsConstructor
@NoArgsConstructor
public class CommitDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hash;

    @Column(name = "commit_date")
    private Date commitDate;

    @Column(name = "author_date")
    private Date authorDate;

    @Column(name = "branch_id")
    private long branchId;

    private String committer;

    private String author;

    private String message;

    public CommitDTO(String hash,
                     Date commitDate,
                     Date authorDate,
                     long branchId,
                     String committer,
                     String author,
                     String message) {
        this.hash = hash;
        this.commitDate = commitDate;
        this.authorDate = authorDate;
        this.branchId = branchId;
        this.committer = committer;
        this.author = author;
        this.message = message;
    }
}
