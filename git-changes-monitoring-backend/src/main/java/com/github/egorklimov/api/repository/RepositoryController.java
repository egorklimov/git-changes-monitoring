package com.github.egorklimov.api.repository;


import com.github.egorklimov.api.repository.model.GitRepositoryCloneRequest;
import com.github.egorklimov.api.repository.model.GitRepositoryResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Tag(name="repository")
@Path("/api/repository")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RepositoryController {

    @Inject
    RepositoryService service;

    @POST
    @Path("/clone")
    public GitRepositoryResponse create(GitRepositoryCloneRequest cloneRequest) {
        return new GitRepositoryResponse(
                service.cloneRepository(
                        cloneRequest.getUrl()
                )
        );
    }
}
