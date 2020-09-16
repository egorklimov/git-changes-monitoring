package com.github.egorklimov.engine;

import com.github.egorklimov.data.repository.contributor.Contributor;
import org.eclipse.jgit.lib.PersonIdent;

import java.util.function.Function;

class ContributorFromPersonIndent implements Function<PersonIdent, Contributor> {

    @Override
    public Contributor apply(final PersonIdent personIdent) {
        return new Contributor(
                personIdent.getEmailAddress(),
                personIdent.getName()
        );
    }
}
