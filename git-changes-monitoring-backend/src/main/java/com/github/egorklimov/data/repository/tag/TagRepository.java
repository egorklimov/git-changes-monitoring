package com.github.egorklimov.data.repository.tag;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TagRepository implements PanacheRepository<Tag> {
}
