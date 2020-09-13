package com.github.egorklimov.data.repository.commit;

import com.github.egorklimov.data.repository.contributor.Contributor;
import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "commit")
public class Commit {
    @Id
    private String hash;

    @Column(name = "commit_date")
    private LocalDate commitDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "repository", referencedColumnName = "id")
    private GitRepository repository;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author", referencedColumnName = "mail")
    private Contributor author;

    private String message;
}
