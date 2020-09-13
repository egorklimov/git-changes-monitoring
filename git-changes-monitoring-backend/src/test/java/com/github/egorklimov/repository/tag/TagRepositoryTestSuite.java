package com.github.egorklimov.repository.tag;

import com.github.egorklimov.data.repository.tag.Tag;
import com.github.egorklimov.repository.CommonCRUDTestSuite;

import java.util.function.Predicate;

public class TagRepositoryTestSuite implements CommonCRUDTestSuite<Tag> {

    @Override
    public Tag valueToPersist() {
        Tag tag = new Tag();
        tag.setName("test");
        tag.setDescription("test tag");
        return tag;
    }

    @Override
    public String updateQuery() {
        return "name = 'newName' where id = 1";
    }

    @Override
    public Predicate<Tag> postUpdateCondition() {
        return tag -> tag.getName().equals("newName");
    }
}
