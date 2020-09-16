package com.github.egorklimov.api.commit;

import com.github.egorklimov.api.commit.model.CommitDtoAsResponse;
import com.github.egorklimov.api.commit.model.CommitResponse;
import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.branch.BranchRepository;
import com.github.egorklimov.data.repository.commit.CommitRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepositoryRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CommitService {

    private final GitRepositoryRepository repository;
    private final BranchRepository branchRepository;
    private final CommitRepository commitRepository;

    @Inject
    public CommitService(GitRepositoryRepository repository,
                         BranchRepository branchRepository,
                         CommitRepository commitRepository) {
        this.repository = repository;
        this.branchRepository = branchRepository;
        this.commitRepository = commitRepository;
    }

    public List<CommitResponse> load(String repositoryName, String branchName) {
        GitRepository loadedRepo = repository.find("name", repositoryName)
                .firstResultOptional()
                .orElseThrow(() -> new NotFoundException("Repository with name " + repositoryName + " not found"));
        Branch loadedBranch = branchRepository.find(
                "short_name = ?1 and repository_id = ?2", branchName, loadedRepo.getId()
        ).firstResultOptional()
                .orElseThrow(() -> new NotFoundException(
                        "Branch with name "
                                + branchName
                                + " not found in repository "
                                + loadedRepo.getName())
                );

        CommitDtoAsResponse formatter = new CommitDtoAsResponse();
        return commitRepository.stream("branch_id", loadedBranch.getId())
                .map(formatter)
                .collect(Collectors.toList());
    }
}
