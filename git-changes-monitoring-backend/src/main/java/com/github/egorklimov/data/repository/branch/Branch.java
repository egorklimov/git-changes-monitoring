package com.github.egorklimov.data.repository.branch;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@Table(name = "branch")
@NoArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "repository_id")
    private long repositoryId;

    @Column(name = "is_scanned")
    private boolean isScanned;

    public Branch(String name,
                  long repositoryId) {
        this.name = name;
        this.repositoryId = repositoryId;
        this.isScanned = false;
        this.shortName = resolveShortName(name);
    }

    private static String resolveShortName(String fullPath) {
        List<String> parts = Arrays.asList(fullPath.split("/"));
        return parts.get(parts.size() - 1);
    }
}
