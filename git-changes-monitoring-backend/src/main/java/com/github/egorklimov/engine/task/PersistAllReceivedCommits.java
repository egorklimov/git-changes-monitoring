package com.github.egorklimov.engine.task;

import com.github.egorklimov.data.repository.commit.CommitRepository;
import com.github.egorklimov.data.transaction.Transaction;
import com.github.egorklimov.entity.Commit;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PersistAllReceivedCommits implements ProgressHandler<List<Commit>> {

    private final Transaction transaction;
    private final CommitRepository commitRepository;

    @Override
    public void accept(List<Commit> commits) {
        transaction.execute(() -> {
            commitRepository.persist(
                    commits.stream()
                            .map(Commit::asDto)
                            .collect(Collectors.toList())
            );
        });
    }
}
