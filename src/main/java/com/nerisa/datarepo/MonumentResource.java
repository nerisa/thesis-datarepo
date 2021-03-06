package com.nerisa.datarepo;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.service.MonumentService;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 2/16/18.
 */
@Path("/monument")
public class MonumentResource {

    private static final Logger LOG = Logger.getLogger(MonumentResource.class.getSimpleName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMonument(Monument monument){
        LOG.log(Level.INFO, "Adding a new monument");
        MonumentService monumentService = new MonumentService();
        try {
            monumentService.saveMonument(monument);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).entity(monument).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id, @QueryParam("compact") boolean compact){
        MonumentService monumentService = new MonumentService();
        Monument monument = monumentService.getMonument(id);
        if(compact){
            JSONObject object = monumentService.getConsolidateMonumentData(monument);
            return Response.status(Response.Status.OK).entity(object).build();
        } else {
            return Response.status(Response.Status.OK).entity(monument).build();
        }
    }
}
