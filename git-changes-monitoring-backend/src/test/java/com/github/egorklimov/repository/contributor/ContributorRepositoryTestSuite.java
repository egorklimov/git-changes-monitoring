package com.github.egorklimov.repository.contributor;

import com.github.egorklimov.data.repository.contributor.Contributor;
import com.github.egorklimov.repository.CommonCRUDTestSuite;

import java.util.function.Predicate;

public class ContributorRepositoryTestSuite implements CommonCRUDTestSuite<Contributor> {

    @Override
    public Contributor valueToPersist() {
        Contributor contributor = new Contributor();
        contributor.setName("qwerty");
        contributor.setMail("qwerty@qwerty.qwerty");
        return contributor;
    }

    @Override
    public String updateQuery() {
        return "mail = 'qwerty@qwerty.com' where name = 'qwerty'";
    }

    @Override
    public Predicate<Contributor> postUpdateCondition() {
        return contributor -> contributor.getMail().equals("qwerty@qwerty.com");
    }
}
