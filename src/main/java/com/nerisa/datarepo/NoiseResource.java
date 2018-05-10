package com.nerisa.datarepo;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.NoiseData;
import com.nerisa.datarepo.service.MonumentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by nerisa on 4/6/18.
 */
@Path("/monument/{id}/noise")
public class NoiseResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNoise(@PathParam("id")Long id, NoiseData noiseData){
        MonumentService monumentService = new MonumentService();
        try {
            Monument monument = monumentService.getMonument(id);
            if(monument != null) {
                monumentService.addNoise(monument, noiseData);
                return Response.status(Response.Status.OK).entity(noiseData).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
