package com.nerisa.datarepo;

import com.nerisa.datarepo.model.Warning;
import com.nerisa.datarepo.service.MonumentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by nerisa on 3/13/18.
 */
@Path("/monument/{id}/warning")
public class WarningResource {



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createWarning(@PathParam("id")Long id, Warning warning){
        Warning savedWarning = MonumentService.addUnverifiedWarning(warning, id);
        return Response.status(Response.Status.OK).entity(savedWarning).build();
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{warningId}")
    public Response modifyWarning(@PathParam("id")Long id, @PathParam("warningId")Long warningId, Warning warning){
        warning.setId(warningId);
        MonumentService.changeWarningStatus(warning, id);
        return Response.status((Response.Status.OK)).entity(warning).build();
    }

}
