package com.github.egorklimov.data.repository.branch;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BranchRepository implements PanacheRepository<Branch> {
}
