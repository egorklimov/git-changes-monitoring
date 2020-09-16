package com.github.egorklimov.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class RepositoryUpdate {
    private final List<BranchUpdate> forcePushedBranches;
    private final List<BranchUpdate> fastForwardedBranches;
}
