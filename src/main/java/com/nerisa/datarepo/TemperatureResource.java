package com.nerisa.datarepo;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.TemperatureData;
import com.nerisa.datarepo.service.MonumentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by nerisa on 4/6/18.
 */
@Path("/monument/{id}/temperature")
public class TemperatureResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTemperature(@PathParam("id")Long id, TemperatureData temperatureData){
        try {
            Monument monument = MonumentService.getMonument(id);
            if(monument != null) {
                MonumentService.addTemperature(monument, temperatureData);
                return Response.status(Response.Status.OK).entity(temperatureData).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
