package com.github.egorklimov.engine;

import com.github.egorklimov.data.repository.contributor.Contributor;
import com.github.egorklimov.data.repository.contributor.ContributorRepository;
import com.github.egorklimov.data.transaction.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.function.UnaryOperator;

@RequiredArgsConstructor
class PersistContributorIfNotExists implements UnaryOperator<Contributor> {

    private final Transaction transaction;
    private final ContributorRepository contributorRepository;

    @Override
    public Contributor apply(final Contributor contributor) {
        transaction.execute(() -> {
            if (contributorRepository.find("mail", contributor.getMail()).count() == 0) {
                try {
                    contributorRepository.persistAndFlush(contributor);
                } catch (Exception ignored) {
                    // on concurrent persist do nothing
                }
            }
        });
        return contributor;
    }
}
