package com.github.egorklimov.entity;

import com.github.egorklimov.data.repository.branch.Branch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.lib.RefUpdate;

@Data
@RequiredArgsConstructor
public class BranchUpdate {
    private final Branch branch;
    private final RefUpdate.Result result;
    private final String oldHash;
    private final String newHash;
}
