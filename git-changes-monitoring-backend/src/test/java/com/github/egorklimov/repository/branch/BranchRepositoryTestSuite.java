package com.github.egorklimov.repository.branch;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepositoryRepository;
import com.github.egorklimov.data.transaction.Transaction;
import com.github.egorklimov.repository.CommonCRUDTestSuite;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class BranchRepositoryTestSuite implements CommonCRUDTestSuite<Branch> {

    private final Transaction transaction;
    private final GitRepositoryRepository repository;

    @SneakyThrows
    @Override
    public Branch valueToPersist() {
        GitRepository gitRepository = new GitRepository();
        gitRepository.setName("test_branch");
        gitRepository.setPath("test_branch");
        gitRepository.setIsSyncing(false);
        transaction.execute(() -> repository.persistAndFlush(gitRepository));

        Branch branch = new Branch();
        branch.setName("master");
        branch.setShortName(branch.getName());
        branch.setRepositoryId(repository.find("name", "test_branch").firstResult().getId());
        return branch;
    }

    @Override
    public String updateQuery() {
        return "name = 'develop' where name = 'master' " +
                "and repository_id IN (select id from GitRepository where name = 'test_branch')";
    }

    @Override
    public Predicate<Branch> postUpdateCondition() {
        return branch -> branch.getName().equals("develop");
    }
}
