package com.nerisa.datarepo;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.Post;
import com.nerisa.datarepo.service.MonumentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by nerisa on 3/14/18.
 */
@Path("/monument/{id}/post")
public class PostResource {

    MonumentService monumentService = new MonumentService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPost(@PathParam("id")Long id, Post post){
        Monument monument = MonumentService.getMonument(id);
        Post savedPost = MonumentService.addPost(post, monument);
        return Response.status(Response.Status.OK).entity(savedPost).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosts(@PathParam("id") Long id){
        List<Post> postList = MonumentService.getPosts(id);
        return Response.status(Response.Status.OK).entity(postList).build();
    }

}
