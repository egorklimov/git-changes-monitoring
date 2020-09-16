package com.github.egorklimov.engine.executor;

import com.github.egorklimov.data.repository.branch.Branch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListAllBranches implements Supplier<List<Branch>> {

    private final Git git;
    private final long targetRepositoryId;

    @SneakyThrows
    public List<Branch> get() {
        return git.branchList()
                .setListMode(ListBranchCommand.ListMode.REMOTE)
                .call()
                .stream()
                .map(ref -> new Branch(ref.getName(), targetRepositoryId))
                .collect(Collectors.toList());
    }
}
