package com.github.egorklimov.repository.commit;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.branch.BranchRepository;
import com.github.egorklimov.data.repository.commit.CommitDTO;
import com.github.egorklimov.data.repository.contributor.Contributor;
import com.github.egorklimov.data.repository.contributor.ContributorRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepositoryRepository;
import com.github.egorklimov.data.transaction.Transaction;
import com.github.egorklimov.repository.CommonCRUDTestSuite;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CommitRepositoryTestSuite implements CommonCRUDTestSuite<CommitDTO> {

    private final Transaction transaction;
    private final GitRepositoryRepository repository;
    private final BranchRepository branchRepository;
    private final ContributorRepository contributorRepository;

    @SneakyThrows
    @Override
    public CommitDTO valueToPersist() {
        GitRepository gitRepository = new GitRepository();
        gitRepository.setName("test_commit");
        gitRepository.setPath("test_commit");
        gitRepository.setIsSyncing(false);
        transaction.execute(() -> repository.persistAndFlush(gitRepository));

        Branch branch = new Branch();
        branch.setRepositoryId(gitRepository.getId());
        branch.setName("test_commit");
        branch.setShortName(branch.getName());
        transaction.execute(() -> branchRepository.persistAndFlush(branch));

        Contributor contributor = new Contributor();
        contributor.setMail("klimovgeor@yandex.ru");
        contributor.setName("egorklimov");
        transaction.execute(() -> contributorRepository.persistAndFlush(contributor));

        CommitDTO commit = new CommitDTO();
        commit.setBranchId(branch.getId());
        commit.setAuthor(contributor.getMail());
        commit.setCommitter(contributor.getMail());
        commit.setAuthorDate(Date.from(Instant.now()));
        commit.setCommitDate(Date.from(Instant.now()));
        commit.setHash(UUID.randomUUID().toString());
        commit.setMessage("test: check commit insert operation");
        return commit;
    }

    @Override
    public String updateQuery() {
        return "message = 'test: check commit update operation' " +
                "where author in (select mail from Contributor where name = 'egorklimov')" +
                "and branch_id in (" +
                "   select id from Branch where repository_id in " +
                "       (select id from GitRepository " +
                "        where name = 'test_commit')" +
                ")";
    }

    @Override
    public Predicate<CommitDTO> postUpdateCondition() {
        return commit -> commit.getMessage().equals("test: check commit update operation");
    }
}
