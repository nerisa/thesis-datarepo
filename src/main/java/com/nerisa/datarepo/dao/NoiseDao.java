package com.nerisa.datarepo.dao;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.NoiseData;
import com.nerisa.datarepo.ontology.Connection;
import com.nerisa.datarepo.ontology.SosaSchema;
import com.nerisa.datarepo.utils.Constant;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;

/**
 * Created by nerisa on 3/27/18.
 */
public class NoiseDao {

//    private static OntModel model = Connection.getModel();

    public static void addNoiseData(NoiseData noiseData, Monument monument){
        OntModel model = Connection.getModel();
        Connection.openDataSetForWrite();
        String noiseResourceUri = noiseData.retrieveNoiseUri(monument);
        try {
            Individual noiseIndividual = model.createIndividual(noiseResourceUri, SosaSchema.OBSERVATION);
            noiseIndividual.addProperty(SosaSchema.HAS_FEATURE_OF_INTEREST, model.getIndividual(monument.getMonumentUri() + Constant.LOCATION_URI));
            noiseIndividual.addProperty(SosaSchema.OBSERVES, getNoiseTypeResource(model));
            noiseIndividual.addProperty(SosaSchema.HAS_SIMPLE_RESULT, noiseData.getFile());
            noiseIndividual.addLiteral(SosaSchema.RESULT_TIME, noiseData.getDate());
            model.commit();
        }finally {
            Connection.closeConnections();
        }

    }

    public static Individual getNoiseTypeResource(OntModel model){
        Individual noiseIndividual = model.getIndividual("http://com.nerisa.thesis/1.0#noise");
        if (noiseIndividual == null) {
            noiseIndividual = model.createIndividual("http://com.nerisa.thesis/1.0#noise", SosaSchema.OBSERVED_PROPERTY);
        }
        return  noiseIndividual;
    }

    public static NoiseData getNoiseData(long latestNoiseId, long monumentId) {
        OntModel model = Connection.getModel();
        Connection.openDataSetForRead();
        try {
           NoiseData noiseData = new NoiseData();
           noiseData.setId(latestNoiseId);
           String noiseResourceUrl = noiseData.retrieveNoiseUri(monumentId);
           Individual noiseResource = model.getIndividual(noiseResourceUrl);
           noiseData.setDate(noiseResource.getProperty(SosaSchema.RESULT_TIME).getLong());
           noiseData.setFile(noiseResource.getProperty(SosaSchema.HAS_SIMPLE_RESULT).getString());
           return noiseData;
       } finally {
            Connection.closeConnections();
       }
    }
}
