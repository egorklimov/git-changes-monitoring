package com.github.egorklimov.data.repository.branch;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BranchRepository implements PanacheRepository<Branch> {
    public List<Branch> notScanned(long repositoryId) {
        return list("is_scanned = ?1 and repository_id = ?2", false, repositoryId);
    }
}
