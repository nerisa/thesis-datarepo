package com.nerisa.datarepo.dao;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.Warning;
import com.nerisa.datarepo.ontology.CidocSchema;
import com.nerisa.datarepo.ontology.Connection;
import com.nerisa.datarepo.ontology.SosaSchema;
import com.nerisa.datarepo.utils.Constant;
import com.nerisa.datarepo.utils.Utility;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 3/26/18.
 */
public class WarningDao {

    private static final OntModel model = Connection.getModel();
    private static final Logger LOG = Logger.getLogger(WarningDao.class.getSimpleName());

    public static void createWarning(Warning warning, Monument monument, Long warningResourceId){
        String resourceBaseUri = monument.getMonumentUri();
        Individual monumentIndividual = model.getIndividual(resourceBaseUri);
        createWarning(warning, monumentIndividual, warningResourceId);

    }

    public static void createWarning(Warning warning, Resource monumentResource, Long warningResourceId){
        String warningUri = monumentResource.getURI() + Constant.WARNING_BASE_URI + "/" + warningResourceId;

        Individual warningIndividual = model.createIndividual(warningUri, CidocSchema.E5_EVENT);

        Individual warningType = model.createIndividual(Constant.WARNING_TYPE_URI, CidocSchema.E55_TYPE);

        Individual warningPic = model.createIndividual(warning.getImage(), CidocSchema.E38_IMAGE);
        warningPic.addProperty(CidocSchema.P138_REPRESENTS, warningIndividual);

        Individual warningTime = model.createIndividual(warningUri + "/" + "time", CidocSchema.E52_TIME_SPAN);
        warningTime.addLiteral(CidocSchema.P82_AT_SOME_TIME_WITHIN, warning.getDate());

        warningIndividual.addProperty(CidocSchema.P20_HAD_SPECIFIC_PURPOSE, warningType);
        warningIndividual.addProperty(CidocSchema.P3_HAS_NOTE, warning.getDesc());
        warningIndividual.addProperty(CidocSchema.P4_HAS_TIME_SPAN,warningTime);
        warningIndividual.addProperty(CidocSchema.P8_TOOK_PLACE_ON_OR_WITHIN, monumentResource);

        model.commit();
    }

    public static List<Warning> getVerifiedWarnings(Monument monument){
        List<Warning> warnings = new ArrayList<Warning>();
        String monumentResourceUri = monument.getMonumentUri();

        String queryString = "SELECT ?warning WHERE {" +
                "   ?warning a <" + CidocSchema.E5_EVENT + "> ." +
                "   ?warning <" + CidocSchema.P20_HAD_SPECIFIC_PURPOSE + "> <" + Constant.WARNING_TYPE_URI + "> ." +
                "   ?warning <" + CidocSchema.P8_TOOK_PLACE_ON_OR_WITHIN + "> <" + monumentResourceUri + "> }";
        LOG.log(Level.INFO, "Getting verified warnings for the monument " + monument.getName() + "with query: ");
        LOG.log(Level.INFO, queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try{
            ResultSet results = qexec.execSelect();
            for (; results.hasNext(); ){
                QuerySolution soln = results.nextSolution();
                Resource warningResource = (Resource) soln.get("?warning");
                LOG.log(Level.INFO, warningResource.getURI());
                warnings.add(getWarningDetails(warningResource));
            }
        } finally {
            qexec.close();
        }
        return warnings;
    }

    public static Warning getWarningDetails(Resource warningResource){
        Resource timeResource = (Resource) warningResource.getProperty(CidocSchema.P4_HAS_TIME_SPAN).getObject();
        Long time = timeResource.getProperty(CidocSchema.P82_AT_SOME_TIME_WITHIN).getLong();
        String desc = warningResource.getProperty(CidocSchema.P3_HAS_NOTE).getString();
        String image = MonumentDao.getImage(warningResource.getURI());
        Long warningId = Utility.getMonumentChildResourceId(warningResource.getURI());
        return new Warning(warningId, desc, image, time, Boolean.TRUE);


    }
}
