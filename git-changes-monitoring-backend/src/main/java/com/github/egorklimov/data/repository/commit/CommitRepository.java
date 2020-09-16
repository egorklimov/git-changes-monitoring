package com.github.egorklimov.data.repository.commit;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommitRepository implements PanacheRepository<CommitDTO> {

    public long countByBranch(long branchId) {
        return find("branch_id", branchId).count();
    }

    public long countOfCommitsAhead(long branchId, String commitHash) {
        return find(
                "branch_id = ?1 and id > (select id from CommitDTO where hash = ?2)",
                branchId,
                commitHash
        ).count();
    }
}
