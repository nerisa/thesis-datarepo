package com.nerisa.datarepo.dao;

import com.nerisa.datarepo.model.Monument;
import com.nerisa.datarepo.model.TemperatureData;
import com.nerisa.datarepo.ontology.Connection;
import com.nerisa.datarepo.ontology.SosaSchema;
import com.nerisa.datarepo.utils.Constant;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 3/27/18.
 */
public class TemperatureDao {

//    private static OntModel model = Connection.getModel();
    private static final Logger LOG = Logger.getLogger(TemperatureDao.class.getSimpleName());

    public static void addTemperatureData(TemperatureData temperatureData, Monument monument){
        OntModel model = Connection.getModel();
        Connection.openDataSetForWrite();
        LOG.log(Level.INFO, "Adding temperature data for monument " + monument.getName());
        try {
            String temperatureResourceUri = temperatureData.retrieveTemperatureUri(monument);
            Individual placeIndividual = model.getIndividual(monument.getMonumentUri() + Constant.LOCATION_URI);

            Individual temperatureIndividual = model.createIndividual(temperatureResourceUri, SosaSchema.OBSERVATION);
            temperatureIndividual.addProperty(SosaSchema.OBSERVES, getTemperatureTypeResource(model));

            Individual resultIndividual = model.createIndividual(temperatureResourceUri + Constant.RESULT_URI, SosaSchema.RESULT);
            resultIndividual.addLiteral(model.getProperty(Constant.QUDT + "numericValue"), temperatureData.getValue());
            resultIndividual.addProperty(model.getProperty(Constant.QUDT + "unit"), getUnitIndividual(model));

            temperatureIndividual.addLiteral(SosaSchema.RESULT_TIME, temperatureData.getDate());
            temperatureIndividual.addProperty(SosaSchema.HAS_RESULT, resultIndividual);
            temperatureIndividual.addProperty(SosaSchema.HAS_FEATURE_OF_INTEREST, placeIndividual);
            temperatureIndividual.addProperty(SosaSchema.MADE_BY_SENSOR, getSensorIndividual(model));

            model.commit();
        }finally {
            Connection.closeConnections();
        }

    }

    public static Individual getTemperatureTypeResource(OntModel model){
        Individual tempIndividual = model.getIndividual("http://com.nerisa.thesis/1.0#temperature");
        if (tempIndividual == null) {
            tempIndividual = model.createIndividual("http://com.nerisa.thesis/1.0#temperature", SosaSchema.OBSERVED_PROPERTY);
        }
        return tempIndividual;
    }

    public static Individual getUnitIndividual(OntModel model){
        Individual unitIndividual = model.getIndividual("http://com.nerisa.thesis/1.0#DegreeCelcius");
        if (unitIndividual == null) {
            unitIndividual = model.createIndividual("http://com.nerisa.thesis/1.0#DegreeCelcius", model.getOntClass(Constant.QUDT + "TemperatureUnit"));
        }
        return unitIndividual;
    }

    public static Individual getSensorIndividual(OntModel model){
        Individual sensorIndividual = model.getIndividual("http://api.openweathermap.org/data/2.5/weather");
        if (sensorIndividual == null) {
            sensorIndividual = model.createIndividual("http://api.openweathermap.org/data/2.5/weather", SosaSchema.SENSOR);
        }
        return sensorIndividual;
    }

}
