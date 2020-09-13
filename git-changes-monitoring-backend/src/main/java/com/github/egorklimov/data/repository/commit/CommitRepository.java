package com.github.egorklimov.data.repository.commit;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommitRepository implements PanacheRepository<Commit> {
}
