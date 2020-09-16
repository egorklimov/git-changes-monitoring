package com.github.egorklimov.data.repository.gitrepository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class GitRepositoryRepository implements PanacheRepository<GitRepository> {
    public List<GitRepository> syncingRepositories() {
        return list("is_syncing = ?1", true);
    }
}
