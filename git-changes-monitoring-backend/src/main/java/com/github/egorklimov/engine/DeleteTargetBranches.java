package com.github.egorklimov.engine;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.branch.BranchRepository;
import com.github.egorklimov.data.transaction.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DeleteTargetBranches {

    private final List<Branch> targetBranches;
    private final Transaction transaction;
    private final BranchRepository branchRepository;

    public void apply() {
        targetBranches.forEach(branch ->
                transaction.execute(() -> branchRepository.delete(
                        "name = ?1 and repository_id = ?2",
                        branch.getName(),
                        branch.getRepositoryId()
                )));
    }

}
