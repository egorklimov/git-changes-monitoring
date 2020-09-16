package com.github.egorklimov.engine.executor;

import com.github.egorklimov.data.repository.branch.Branch;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class BranchTaskIdentifier implements Supplier<String> {

    private final Branch branch;

    @Override
    public String get() {
        return branch.getName();
    }
}
