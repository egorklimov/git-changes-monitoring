package com.github.egorklimov.repository;

import com.github.egorklimov.data.repository.branch.BranchRepository;
import com.github.egorklimov.data.repository.commit.CommitRepository;
import com.github.egorklimov.data.repository.contributor.ContributorRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepositoryRepository;
import com.github.egorklimov.data.repository.tag.TagRepository;
import com.github.egorklimov.data.transaction.Transaction;
import com.github.egorklimov.repository.branch.BranchRepositoryTestSuite;
import com.github.egorklimov.repository.commit.CommitRepositoryTestSuite;
import com.github.egorklimov.repository.contributor.ContributorRepositoryTestSuite;
import com.github.egorklimov.repository.gitrepository.GitRepositoryTestSuite;
import com.github.egorklimov.repository.tag.TagRepositoryTestSuite;
import com.github.egorklimov.resource.TestResourceLifecycleManager;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@Tag("integration")
@QuarkusTest
@QuarkusTestResource(TestResourceLifecycleManager.class)
class CommonRepositoryTest {

    private final Transaction transaction;
    private final GitRepositoryRepository gitRepositoryRepository;
    private final TagRepository tagRepository;
    private final BranchRepository branchRepository;
    private final CommitRepository commitRepository;
    private final ContributorRepository contributorRepository;

    @Inject
    public CommonRepositoryTest(Transaction transaction,
                                GitRepositoryRepository gitRepositoryRepository,
                                TagRepository tagRepository,
                                BranchRepository branchRepository,
                                CommitRepository commitRepository,
                                ContributorRepository contributorRepository) {
        this.transaction = transaction;
        this.gitRepositoryRepository = gitRepositoryRepository;
        this.tagRepository = tagRepository;
        this.branchRepository = branchRepository;
        this.commitRepository = commitRepository;
        this.contributorRepository = contributorRepository;
    }

    @Test
    void checkGitRepositoryCRUDOperations() {
        new GitRepositoryTestSuite().run(gitRepositoryRepository, transaction);
    }

    @Test
    void checkTagRepositoryCRUDOperations() {
        new TagRepositoryTestSuite().run(tagRepository, transaction);
    }

    @Test
    void checkBranchRepositoryCRUDOperations() {
        new BranchRepositoryTestSuite(transaction, gitRepositoryRepository).run(branchRepository, transaction);
    }

    @Test
    void checkCommitRepositoryCRUDOperations() {
        new CommitRepositoryTestSuite(transaction, gitRepositoryRepository, branchRepository, contributorRepository).run(commitRepository, transaction);
    }

    @Test
    void checkContributorRepositoryCRUDOperations() {
        new ContributorRepositoryTestSuite().run(contributorRepository, transaction);
    }
}
