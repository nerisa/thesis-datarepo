package com.nerisa.datarepo;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.Warning;
import com.nerisa.datarepo.service.MonumentService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by nerisa on 3/29/18.
 */
@Path("/monument/{id}/warnings")
public class WarningsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWarnings(@PathParam("id")Long id){
        Monument monument = MonumentService.getMonument(id);
        List<Warning> warningList = MonumentService.getWarnings(monument);
        List<Warning> unverifiedWarning = MonumentService.getUnverifiedWarnings(monument);
        warningList.addAll(unverifiedWarning);
        return Response.status(Response.Status.OK).entity(warningList).build();
    }
}
