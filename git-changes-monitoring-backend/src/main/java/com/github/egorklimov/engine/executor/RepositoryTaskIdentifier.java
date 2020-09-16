package com.github.egorklimov.engine.executor;

import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class RepositoryTaskIdentifier implements Supplier<String> {

    private final GitRepository gitRepository;

    @Override
    public String get() {
        return gitRepository.getPath();
    }
}
