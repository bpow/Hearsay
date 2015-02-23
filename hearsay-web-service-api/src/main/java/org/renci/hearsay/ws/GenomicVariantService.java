package org.renci.hearsay.ws;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.renci.hearsay.dao.model.GenomicVariant;

@Path("/GenomicVariantService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface GenomicVariantService {

    @GET
    @Path("/findById/{id}")
    public GenomicVariant findById(@PathParam("id") Long id);

    @GET
    @Path("/findByGeneName/{name}")
    public List<GenomicVariant> findByGeneName(@PathParam("name") String name);

}
