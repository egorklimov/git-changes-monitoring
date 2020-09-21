package com.github.egorklimov.api.repository;

import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepositoryRepository;
import com.github.egorklimov.data.repository.tag.RepositoryHasTag;
import com.github.egorklimov.data.repository.tag.RepositoryHasTagRepository;
import com.github.egorklimov.data.repository.tag.Tag;
import com.github.egorklimov.data.repository.tag.TagRepository;
import com.github.egorklimov.data.transaction.Transaction;
import com.github.egorklimov.git.CloneRemoteRepository;
import com.github.egorklimov.git.RepositoryLocalStorageConfiguration;
import lombok.SneakyThrows;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class RepositoryService {

    private final RepositoryHasTagRepository repositoryHasTagRepository;
    private final RepositoryLocalStorageConfiguration configuration;
    private final GitRepositoryRepository repository;
    private final TagRepository tagRepository;
    private final Transaction transaction;

    @Inject
    public RepositoryService(RepositoryHasTagRepository repositoryHasTagRepository,
                             RepositoryLocalStorageConfiguration configuration,
                             GitRepositoryRepository repository,
                             TagRepository tagRepository,
                             Transaction transaction) {
        this.repositoryHasTagRepository = repositoryHasTagRepository;
        this.configuration = configuration;
        this.repository = repository;
        this.tagRepository = tagRepository;
        this.transaction = transaction;
    }

    @SneakyThrows
    public String cloneRepository(String url) {
        GitRepository cloned = new CloneRemoteRepository().apply(url, configuration.getStorage());

        Optional<Tag> loadedTag = transaction.execute(() ->
                tagRepository.find(
                        "name",
                        new TagNameFromReposioryUrl(url).get()
                )
        ).firstResultOptional();
        final Tag tag;
        if (loadedTag.isEmpty()) {
            tag = new Tag(new TagNameFromReposioryUrl(url).get());
            transaction.execute(() -> tagRepository.persistAndFlush(tag));
        } else {
            tag = loadedTag.get();
        }

        transaction.execute(() -> repository.persistAndFlush(cloned));
        transaction.execute(() -> repositoryHasTagRepository.persist(new RepositoryHasTag(tag.getId(), cloned.getId())));
        return cloned.getPath();
    }
}
