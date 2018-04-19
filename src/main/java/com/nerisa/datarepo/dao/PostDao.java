package com.nerisa.datarepo.dao;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.Post;
import com.nerisa.datarepo.ontology.CidocSchema;
import com.nerisa.datarepo.ontology.Connection;
import com.nerisa.datarepo.utils.Constant;
import com.nerisa.datarepo.utils.Utility;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 3/27/18.
 */
public class PostDao {

//    private static final OntModel model = Connection.getModel();

    private static final Logger LOG = Logger.getLogger(PostDao.class.getSimpleName());

    public static void createPost(Post post, Monument monument) {
        OntModel model = Connection.getModel();
        Connection.openDataSetForWrite();
        LOG.log(Level.INFO, "Creating a new post for monument " + monument.getName());
        try {
            String resourceBaseUri = monument.getMonumentUri();
            String postBaseUri = resourceBaseUri + Constant.POST_BASE_URI + "/" + post.getId();
            Individual monumentIndividual = model.getIndividual(resourceBaseUri);
            Individual postIndividual = model.createIndividual(postBaseUri, CidocSchema.E5_EVENT);

            Individual postType = model.createIndividual(Constant.POST_TYPE_URI, CidocSchema.E55_TYPE);

            Individual postTime = model.createIndividual(postBaseUri + "/" + "time", CidocSchema.E52_TIME_SPAN);
            postTime.addLiteral(CidocSchema.P82_AT_SOME_TIME_WITHIN, post.getDate());

            postIndividual.addProperty(CidocSchema.P8_TOOK_PLACE_ON_OR_WITHIN, monumentIndividual);
            postIndividual.addProperty(CidocSchema.P2_HAS_TYPE, postType);
            postIndividual.addProperty(CidocSchema.P4_HAS_TIME_SPAN, postTime);
            postIndividual.addProperty(CidocSchema.P3_HAS_NOTE, post.getDesc());

            model.commit();
        } finally {
            Connection.closeConnections();
        }

    }

    public static List<Post> getPosts(Monument monument) {
        OntModel model = Connection.getModel();
        Connection.openDataSetForRead();
        LOG.log(Level.INFO, "Getting posts for monument " + monument.getName());

        List<Post> posts = new ArrayList<Post>();
        String monumentResourceUri = monument.getMonumentUri();

        String queryString = "SELECT ?post WHERE {" +
                "   ?post a <" + CidocSchema.E5_EVENT + "> ." +
                "   ?post <" + CidocSchema.P2_HAS_TYPE + "> <" + Constant.POST_TYPE_URI + "> ." +
                "   ?post <" + CidocSchema.P8_TOOK_PLACE_ON_OR_WITHIN + "> <" + monumentResourceUri + "> }";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                Resource postResource = (Resource) soln.get("?post");
                posts.add(getPostDetails(postResource));
            }
        } finally {
            qexec.close();
            Connection.closeConnections();
        }
        return posts;
    }

    public static Post getPostDetails(Resource postResource) {

        Resource timeResource = (Resource) postResource.getProperty(CidocSchema.P4_HAS_TIME_SPAN).getObject();
        Long time = timeResource.getProperty(CidocSchema.P82_AT_SOME_TIME_WITHIN).getLong();
        String desc = postResource.getProperty(CidocSchema.P3_HAS_NOTE).getString();
        Long postId = Utility.getMonumentChildResourceId(postResource.getURI());
        return new Post(postId, desc, time);

    }
}
