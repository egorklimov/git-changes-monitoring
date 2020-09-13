package com.github.egorklimov.repository.branch;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.repository.CommonCRUDTestSuite;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class BranchRepositoryTestSuite implements CommonCRUDTestSuite<Branch> {

    @Override
    public Branch valueToPersist() {
        GitRepository gitRepository = new GitRepository();
        gitRepository.setName("test_branch");
        gitRepository.setPath("test_branch");

        Branch branch = new Branch();
        branch.setName("master");
        branch.setRepository(gitRepository);
        return branch;
    }

    @Override
    public String updateQuery() {
        return "name = 'develop' where name = 'master' " +
                "and repository IN (select id from GitRepository where name = 'test_branch')";
    }

    @Override
    public Predicate<Branch> postUpdateCondition() {
        return branch -> branch.getName().equals("develop");
    }
}
