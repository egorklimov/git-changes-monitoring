package com.github.egorklimov.api.tag;

import com.github.egorklimov.api.tag.model.BranchResponse;
import com.github.egorklimov.api.tag.model.RepositoryResponse;
import com.github.egorklimov.api.tag.model.TagResponse;
import com.github.egorklimov.data.repository.branch.BranchRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepositoryRepository;
import com.github.egorklimov.data.repository.tag.RepositoryHasTagRepository;
import com.github.egorklimov.data.repository.tag.TagRepository;
import com.github.egorklimov.data.transaction.Transaction;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
class TagService {

  private final RepositoryHasTagRepository repositoryHasTagRepository;
  private final GitRepositoryRepository gitRepository;
  private final BranchRepository branchRepository;
  private final TagRepository tagRepository;
  private final Transaction transaction;

  @Inject
  TagService(RepositoryHasTagRepository repositoryHasTagRepository,
             GitRepositoryRepository gitRepository,
             BranchRepository branchRepository,
             TagRepository tagRepository,
             Transaction transaction) {
    this.repositoryHasTagRepository = repositoryHasTagRepository;
    this.gitRepository = gitRepository;
    this.branchRepository = branchRepository;
    this.tagRepository = tagRepository;
    this.transaction = transaction;
  }

  public List<TagResponse> load() {
    return transaction.execute(() -> tagRepository.listAll())
            .stream()
            .map(tag -> new TagResponse(
                    tag.getName(),
                    transaction.execute(() -> repositoryHasTagRepository.list("tag_id", tag.getId()))
                            .stream()
                            .map(repositoryHasTag -> transaction.execute(() -> gitRepository.findById(repositoryHasTag.getRepositoryId())))
                            .map(repository -> new RepositoryResponse(
                                    repository.getName(),
                                    transaction.execute(() -> branchRepository.list("repository_id", repository.getId()))
                                            .stream()
                                            .map(branch -> new BranchResponse(branch.getShortName()))
                                            .collect(Collectors.toList()))
                            ).collect(Collectors.toList())
                    )
            ).collect(Collectors.toList());
  }
}
