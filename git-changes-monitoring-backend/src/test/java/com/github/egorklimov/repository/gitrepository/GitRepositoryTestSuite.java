package com.github.egorklimov.repository.gitrepository;

import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.repository.CommonCRUDTestSuite;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class GitRepositoryTestSuite implements CommonCRUDTestSuite<GitRepository> {

    @Override
    public GitRepository valueToPersist() {
        GitRepository repository = new GitRepository();
        repository.setPath("path");
        repository.setName("name");
        repository.setIsSyncing(true);
        return repository;
    }

    @Override
    public String updateQuery() {
        return "name = 'newName' where path = 'path'";
    }

    @Override
    public Predicate<GitRepository> postUpdateCondition() {
        return r -> r.getName().equals("newName");
    }
}
