package com.github.egorklimov.service;

import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepositoryRepository;
import com.github.egorklimov.data.transaction.Transaction;
import com.github.egorklimov.git.CloneRemoteRepository;
import com.github.egorklimov.git.RepositoryLocalStorageConfiguration;
import lombok.SneakyThrows;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class RepositoryService {

    private final RepositoryLocalStorageConfiguration configuration;
    private final GitRepositoryRepository repository;
    private final Transaction transaction;

    @Inject
    public RepositoryService(RepositoryLocalStorageConfiguration configuration,
                             GitRepositoryRepository repository,
                             Transaction transaction) {
        this.configuration = configuration;
        this.repository = repository;
        this.transaction = transaction;
    }

    @SneakyThrows
    public String cloneRepository(String url) {
        GitRepository cloned = new CloneRemoteRepository().apply(url, configuration.getStorage());
        transaction.execute(() -> repository.persist(cloned));
        return cloned.getPath();
    }
}
