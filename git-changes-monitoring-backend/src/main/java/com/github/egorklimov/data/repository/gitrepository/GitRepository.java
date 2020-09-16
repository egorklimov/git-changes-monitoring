package com.github.egorklimov.data.repository.gitrepository;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "repository")
@NoArgsConstructor
public class GitRepository  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    private String name;
    @Column(name = "is_syncing")
    private Boolean isSyncing;

    public GitRepository(String path, String name, Boolean isSyncing) {
        this.path = path;
        this.name = name;
        this.isSyncing = isSyncing;
    }
}
