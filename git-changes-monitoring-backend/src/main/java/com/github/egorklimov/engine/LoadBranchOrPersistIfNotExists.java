package com.github.egorklimov.engine;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.branch.BranchRepository;
import com.github.egorklimov.data.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.UnaryOperator;

@Slf4j
@RequiredArgsConstructor
public class LoadBranchOrPersistIfNotExists implements UnaryOperator<Branch> {

    private final Transaction transaction;
    private final BranchRepository branchRepository;

    @Override
    public Branch apply(Branch branch) {
        log.debug("Trying to persists branch: {}", branch);

        Optional<Branch> founded = branchRepository.find(
                "name = ?1 and repository_id = ?2",
                branch.getName(), branch.getRepositoryId()
        ).firstResultOptional();

        if (founded.isEmpty()) {
            transaction.execute(() -> branchRepository.persistAndFlush(branch));
            log.debug("Persisted branch[{}]", branch);
        } else {
            log.debug("Branch already exists: {}", founded.get());
            return founded.get();
        }
        return branch;
    }
}
