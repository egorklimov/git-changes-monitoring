package com.github.egorklimov.api.commit.model;

import com.github.egorklimov.data.repository.commit.CommitDTO;

import java.text.SimpleDateFormat;
import java.util.function.Function;

public class CommitDtoAsResponse implements Function<CommitDTO, CommitResponse> {

    @Override
    public CommitResponse apply(final CommitDTO dto) {
        return new CommitResponse(
                dto.getHash(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getCommitDate()),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getAuthorDate()),
                dto.getCommitter(),
                dto.getAuthor(),
                dto.getMessage(),
                new DateDetails(dto.getCommitDate()),
                new DateDetails(dto.getAuthorDate())
        );
    }
}
