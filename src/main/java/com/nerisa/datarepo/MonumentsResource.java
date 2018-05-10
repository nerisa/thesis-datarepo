package com.nerisa.datarepo;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.service.MonumentService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by nerisa on 2/14/18.
 */

@Path("monuments")
public class MonumentsResource {

    MonumentService monumentService = new MonumentService();

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getMonuments(@QueryParam("lat") double latitude, @QueryParam("lon") double longitude, @QueryParam("within") int within){
        MonumentService monumentService = new MonumentService();
        List<Monument> monuments = monumentService.getNearestMonuments(latitude, longitude, within);
        GenericEntity<List<Monument>> entity = new GenericEntity<List<Monument>>(monuments) {};
        return Response.status(200).entity(entity).build();
    }



}
