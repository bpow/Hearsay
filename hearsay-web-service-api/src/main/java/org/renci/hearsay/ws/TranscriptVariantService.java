package org.renci.hearsay.ws;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.renci.hearsay.dao.model.TranscriptVariant;

@Path("/TranscriptVariantService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface TranscriptVariantService {

    @GET
    @Path("/findById/{id}")
    public TranscriptVariant findById(@PathParam("id") Long id);

    @GET
    @Path("/findByGeneName/{name}")
    public List<TranscriptVariant> findByGeneName(@PathParam("name") String name);

    @POST
    @Path("/")
    public Long save(TranscriptVariant transcriptVariant);

}
