package com.github.egorklimov.engine;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.branch.BranchRepository;
import com.github.egorklimov.data.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DeleteInactiveBranches {

    private final long repositoryId;
    private final List<Branch> actualBranches;
    private final Transaction transaction;
    private final BranchRepository branchRepository;

    public void apply() {
        if (actualBranches.isEmpty()) {
            return;
        }
        transaction.execute(() ->
                branchRepository.delete(
                        String.format(
                                "repository_id = %s and id not in (%s)",
                                repositoryId,
                                actualBranches
                                        .stream()
                                        .map(Branch::getId)
                                        .filter(Objects::nonNull)
                                        .map(Objects::toString)
                                        .collect(Collectors.joining(","))
                        )
                )
        );
    }
}
