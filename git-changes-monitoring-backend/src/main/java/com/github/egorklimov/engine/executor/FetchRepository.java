package com.github.egorklimov.engine.executor;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.entity.BranchUpdate;
import com.github.egorklimov.entity.RepositoryUpdate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.transport.TrackingRefUpdate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FetchRepository implements Supplier<RepositoryUpdate> {

    private final Git git;
    private final long repositoryId;

    @SneakyThrows
    public RepositoryUpdate get() {
        Map<RefUpdate.Result, List<BranchUpdate>> updateByStatus =
                git.fetch()
                        .setCheckFetchedObjects(true)
                        .setRemoveDeletedRefs(true)
                        .call()
                        .getTrackingRefUpdates()
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        TrackingRefUpdate::getResult,
                                        Collectors.mapping(
                                                update -> new BranchUpdate(
                                                        new Branch(
                                                                update.getLocalName(),
                                                                repositoryId
                                                        ),
                                                        update.getResult(),
                                                        update.getOldObjectId().getName(),
                                                        update.getNewObjectId().getName()
                                                ),
                                                Collectors.toList()
                                        )
                                )
                        );

        return new RepositoryUpdate(
                updateByStatus.getOrDefault(RefUpdate.Result.FORCED, Collections.emptyList()),
                updateByStatus.getOrDefault(RefUpdate.Result.FAST_FORWARD, Collections.emptyList())
        );
    }
}
