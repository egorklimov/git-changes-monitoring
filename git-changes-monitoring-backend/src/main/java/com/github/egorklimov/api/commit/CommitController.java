package com.github.egorklimov.api.commit;

import com.github.egorklimov.api.commit.model.CommitResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Tag(name="repository")
@Path("/api/commit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommitController {

    @Inject
    CommitService service;

    @GET
    @Path("/{repository}/{branch}")
    public List<CommitResponse> load(@PathParam("repository") String repositoryName,
                                     @PathParam("branch") String branchName) {
        return service.load(
                repositoryName,
                branchName
        );
    }
}