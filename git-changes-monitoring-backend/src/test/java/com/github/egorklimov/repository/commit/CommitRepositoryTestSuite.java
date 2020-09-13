package com.github.egorklimov.repository.commit;

import com.github.egorklimov.data.repository.commit.Commit;
import com.github.egorklimov.data.repository.contributor.Contributor;
import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.repository.CommonCRUDTestSuite;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Predicate;

public class CommitRepositoryTestSuite implements CommonCRUDTestSuite<Commit> {
    @Override
    public Commit valueToPersist() {
        GitRepository repository = new GitRepository();
        repository.setName("test_commit");
        repository.setPath("test_commit");

        Contributor contributor = new Contributor();
        contributor.setMail("klimovgeor@yandex.ru");
        contributor.setName("egorklimov");

        Commit commit = new Commit();
        commit.setRepository(repository);
        commit.setAuthor(contributor);
        commit.setCommitDate(LocalDate.now());
        commit.setHash(UUID.randomUUID().toString());
        commit.setMessage("test: check commit insert operation");
        return commit;
    }

    @Override
    public String updateQuery() {
        return "message = 'test: check commit update operation' " +
                "where author in (select mail from Contributor where name = 'egorklimov')" +
                "and repository in (select id from GitRepository where name = 'test_commit')";
    }

    @Override
    public Predicate<Commit> postUpdateCondition() {
        return commit -> commit.getMessage().equals("test: check commit update operation");
    }
}
