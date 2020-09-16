package com.github.egorklimov.data.repository.contributor;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContributorRepository implements PanacheRepository<Contributor> {

    @Override
    public void persist(Iterable<Contributor> contributors) {
        contributors.forEach(c -> {
            if (find("mail", c.getMail()).count() == 0) {
                persist(c);
            }
        });
    }
}
