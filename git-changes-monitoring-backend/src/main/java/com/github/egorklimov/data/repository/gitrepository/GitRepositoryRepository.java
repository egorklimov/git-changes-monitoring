package com.github.egorklimov.data.repository.gitrepository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GitRepositoryRepository implements PanacheRepository<GitRepository> {
}
