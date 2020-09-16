package com.github.egorklimov.engine;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.contributor.Contributor;
import com.github.egorklimov.engine.task.PollableTask;
import com.github.egorklimov.entity.Commit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

@Slf4j
@RequiredArgsConstructor
public class ScanAllFastForwardedCommits implements PollableTask<Commit> {

    private final Git git;
    private final Branch branch;
    private final String oldHash;
    private final UnaryOperator<Contributor> contributorProcessor;

    private final AtomicBoolean isInterrupted = new AtomicBoolean(false);
    private final BlockingQueue<Commit> scanned = new LinkedBlockingQueue<>();

    /**
     * Collects scanned commits.
     * @return  unmodifiable list of already scanned commits.
     */
    @Override
    public List<Commit> poll() {
        List<Commit> processed = new ArrayList<>();
        scanned.drainTo(processed);

        return Collections.unmodifiableList(processed);
    }

    /**
     * Scans the branch from root.
     * Skips first {@code countOfCommitsToSkip} commits.
     */
    @SneakyThrows
    @Override
    public void run() {
        log.info("Starting scan fast forwarded commits {}, oldHash {}", branch.getName(), oldHash);
        Iterable<RevCommit> commits = git.log()
                .add(git.getRepository().resolve(branch.getName()))
                .call();
        for (var revCommit : commits) {
            if (Thread.currentThread().isInterrupted()
                    || isInterrupted.get()
                    || revCommit.getName().equals(oldHash)) {
                return;
            }
            scanned.add(
                    new Commit(
                            revCommit.getName(),
                            revCommit.getCommitterIdent().getWhen(),
                            revCommit.getAuthorIdent().getWhen(),
                            branch,
                            new ContributorFromPersonIndent()
                                    .andThen(contributorProcessor)
                                    .apply(revCommit.getCommitterIdent()),
                            new ContributorFromPersonIndent()
                                    .andThen(contributorProcessor)
                                    .apply(revCommit.getAuthorIdent()),
                            revCommit.getFullMessage()
                    )
            );
        }
    }

    @Override
    public void interrupt() {
        this.isInterrupted.set(true);
    }

}
