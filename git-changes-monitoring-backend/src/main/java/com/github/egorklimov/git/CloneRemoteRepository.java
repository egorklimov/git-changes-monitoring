package com.github.egorklimov.git;

import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CloneRemoteRepository {

    @SneakyThrows
    public GitRepository apply(String url, String dirPath) {
        Preconditions.checkArgument(!url.isEmpty(), "URL is empty");
        Preconditions.checkArgument(url.endsWith(".git"), "URL must ends with .git");
        File target = Paths.get(dirPath, resolveName(url)).toFile();

        Files.delete(target.toPath());
        target.mkdirs();

        log.info("Cloning from {} to {}", url, dirPath);
        try (Git result = Git.cloneRepository()
                .setURI(url)
                .setDirectory(target)
                .setNoCheckout(true)
                .call()) {
            log.info("Having repository: " + result.getRepository().getDirectory());
            return new GitRepository(
                    result.getRepository().getDirectory().getPath(),
                    resolveName(url),
                    true
            );
        }
    }

    private static String resolveName(String gitPath) {
        List<String> parts = Arrays.asList(gitPath.split("/"));
        String nameWithGitExt = parts.get(parts.size() - 1);
        return nameWithGitExt.substring(0, nameWithGitExt.indexOf("."));
    }
}
