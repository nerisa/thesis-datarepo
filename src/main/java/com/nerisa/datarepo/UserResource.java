package com.nerisa.datarepo;

import com.nerisa.datarepo.model.User;
import com.nerisa.datarepo.service.MonumentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by nerisa on 3/30/18.
 */
@Path("/user")
public class UserResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User user){
        MonumentService monumentService = new MonumentService();
        try {
            user = monumentService.createUser(user);
            if (user.getId() == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            } else {
                return Response.status(Response.Status.CREATED).entity(user).build();
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateUser(@PathParam("id")Long userId, User user){
        MonumentService monumentService = new MonumentService();
        user.setId(userId);
        boolean success = monumentService.updateUserToken(user);
        if(success){
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
