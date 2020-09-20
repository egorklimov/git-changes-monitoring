package com.github.egorklimov.api.tag;

import com.github.egorklimov.api.tag.model.TagResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Tag(name="tag")
@Path("/api/tag")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TagController {

  @Inject
  TagService service;

  @GET
  public List<TagResponse> load() {
    return service.load();
  }
}