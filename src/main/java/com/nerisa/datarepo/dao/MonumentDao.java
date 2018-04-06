package com.nerisa.datarepo.dao;

import com.nerisa.datarepo.model.*;
import com.nerisa.datarepo.ontology.CidocSchema;
import com.nerisa.datarepo.ontology.Connection;
import com.nerisa.datarepo.ontology.GeoSchema;
import com.nerisa.datarepo.ontology.SosaSchema;
import com.nerisa.datarepo.service.MonumentService;
import com.nerisa.datarepo.utils.Constant;
import com.nerisa.datarepo.utils.Utility;
import jdk.nashorn.api.scripting.JSObject;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 3/21/18.
 */
public class MonumentDao {

    private static final String PREFIX = "PREFIX cidoc-crm: <http://www.cidoc-crm.org/cidoc-crm/>";
    private static final String MONUMENT_ONT_CLASS = "cidoc-crm:E22_Man-Made_Object";
    private static final String LOCATION_ONT_CLASS = "cidoc-crm:E53_Place";
    private static final String IMAGE_ONT_CLASS = "cidoc-crm:E38_Image";
    private static final String NAME_ONT_CLASS = "cidoc-crm:E41_Appellation";
    private static final String NAME_ONT_DATA_PROPERTY = "cidoc-crm:P90_has_value";
    private static final String LOCATION_ONT_PROPERTY = "cidoc-crm:P55_has_current_location";
    private static final String NAME_ONT_PROPERTY = "cidoc-crm:P1_is_identified_by";
    private static final String DESC_ONT_PROPERTY = "cidoc-crm:P3_has_note";
    private static final OntModel model = Connection.getModel();
    private static final Logger LOG = Logger.getLogger(MonumentDao.class.getSimpleName());

//    public static void createMonumentWithSparql( Monument monument){
//        Dataset dataset  = Connection.getDataset();
//        String monumentUri = BASE_URI + monument.getId();
//        String locationUri = monumentUri + "/location";
//        String photoUri = monumentUri + "/image";
//        String nameUri = monumentUri + "/name";
//        String descUri = monumentUri + "/desc";
//
//
//        dataset.begin(ReadWrite.WRITE);
//        String queryString =
//                PREFIX + " INSERT DATA {<" +
//                        monumentUri + "> a " + MONUMENT_ONT_CLASS + "};"
//                        +
//                " INSERT DATA {<" +
//                        locationUri + "> a " + LOCATION_ONT_CLASS + "};" +
//                " INSERT DATA {<" +
//                        photoUri + "> a " + IMAGE_ONT_CLASS + "};" +
//                " INSERT DATA {<" +
//                        nameUri + "> a " + NAME_ONT_CLASS + "};" +
//                " INSERT DATA {<" +
//                        monumentUri + "> " + NAME_ONT_PROPERTY + " <" + nameUri + ">};" +
//                " INSERT DATA {<" +
//                        monumentUri + "> " + LOCATION_ONT_PROPERTY + " <" + locationUri + ">};" +
//                " INSERT DATA {<" +
//                        monumentUri + "> " + DESC_ONT_PROPERTY + " \"" + monument.getDesc() + "\"};" +
//                " INSERT DATA {<" +
//                        nameUri + "> " + NAME_ONT_PROPERTY + " \"" + monument.getName() + "\"};" +
//                " INSERT DATA {<"
//
//                ;
//        System.out.println(queryString);
//
//        try {
//            UpdateRequest qExec = UpdateFactory.create(queryString);
//            UpdateProcessor processor = UpdateExecutionFactory.create(qExec, dataset);
//            processor.execute();
//            dataset.commit();
//        } finally {
//            dataset.end();
//        }
//
//    }

    public static void createMonument(Monument monument){
        String monumentIri = monument.getMonumentUri();
        LOG.log(Level.INFO, "Creating new monument with url: " + monumentIri);
        Individual custodianResource = model.createIndividual(monument.getCustodian().createUri(), CidocSchema.E39_ACTOR);

        Individual custodianName = model.createIndividual(custodianResource.getURI()+"/name", CidocSchema.E82_ACTOR_APPELLATION);
        custodianName.addProperty(CidocSchema.P90_HAS_VALUE, monument.getCustodian().getEmail());

        custodianResource.addProperty(CidocSchema.P131_IS_IDENTIFIED_BY, custodianName);

        Individual monumentResource = model.createIndividual(monumentIri, CidocSchema.E22_MAN_MADE_OBJECT);

        Individual locationResource = model.createIndividual(monumentIri+ Constant.LOCATION_URI, CidocSchema.E53_PLACE);
        locationResource.addLiteral(GeoSchema.LAT, monument.getLatitude());
        locationResource.addLiteral(GeoSchema.LONG, monument.getLongitude());
        locationResource.addOntClass(SosaSchema.FEATURE_OF_INTEREST);

        Individual nameResource = model.createIndividual(monumentIri+"/name", CidocSchema.E41_APPELLATION);
        nameResource.addProperty(CidocSchema.P90_HAS_VALUE, monument.getName());

        Individual monumentImage = model.createIndividual(monument.getMonumentPhoto(), CidocSchema.E38_IMAGE);
        monumentImage.setPropertyValue(CidocSchema.P138_REPRESENTS, monumentResource);

        monumentResource.addProperty(CidocSchema.P3_HAS_NOTE, monument.getDesc());
        monumentResource.addProperty(CidocSchema.P55_HAS_CURRENT_LOCATION, locationResource);
        monumentResource.addProperty(CidocSchema.P1_IS_IDENTIFIED_BY, nameResource);
        monumentResource.addProperty(CidocSchema.P50_HAS_CURRENT_KEEPER, custodianResource);

        if(monument.getReference()!= null && !monument.getReference().isEmpty()){
            LOG.log(Level.INFO, "Monument has reference: " + monument.getReference());
            Individual referenceIndividual = model.createIndividual(monument.getReference(), CidocSchema.E31_DOCUMENT);
            monumentResource.addProperty(CidocSchema.P70I_IS_DOCUMENTED_IN, referenceIndividual);
        }
        model.commit();
    }

    public static Monument getMonument(Long id){
        String resourceUri = Monument.createMonumentUri(id);
        Resource monumentResource = model.getResource(resourceUri);
//        for(StmtIterator iterator = monument.listProperties(); iterator.hasNext();){
//            System.out.println(iterator.nextStatement());
//        }
        Monument monument = null;
        if(model.containsResource(monumentResource)) {
            LOG.log(Level.INFO, "Monument exists with id " + id);
            monument = getMonument(monumentResource);
            monument.setId(id);
        }
        return monument;
    }

    public static Monument getMonument (Resource monumentResource){
        Resource place = (Resource) monumentResource.getProperty(CidocSchema.P55_HAS_CURRENT_LOCATION).getObject();
        Double latitude = place.getProperty(GeoSchema.LAT).getDouble();
        Double longitude = place.getProperty(GeoSchema.LONG).getDouble();
        Resource nameResource = (Resource)monumentResource.getProperty(CidocSchema.P1_IS_IDENTIFIED_BY).getObject();
        String name = nameResource.getProperty(CidocSchema.P90_HAS_VALUE).getString();
        String desc = monumentResource.getProperty(CidocSchema.P3_HAS_NOTE).getString();
        String imageUri = getImage(monumentResource.getURI());

        Resource custodian = (Resource) monumentResource.getProperty(CidocSchema.P50_HAS_CURRENT_KEEPER).getObject();
        Long custodianId = Utility.getMonumentChildResourceId(custodian.getURI());
        User user = new User();
        user.setId(custodianId);
        user.setCustodian(Boolean.TRUE);

        Long id = Utility.getMonumentChildResourceId(monumentResource.getURI());

        Monument monument = new Monument(id, name, " ", desc, imageUri, latitude, longitude);
        monument.setCustodian(user);
        if(monumentResource.hasProperty(CidocSchema.P70I_IS_DOCUMENTED_IN)){
            LOG.log(Level.INFO, "Retrieved monument has reference: " + monumentResource.getURI());
            Resource referenceResource = (Resource) monumentResource.getProperty(CidocSchema.P70I_IS_DOCUMENTED_IN).getObject();
            monument.setReference(referenceResource.getURI());
        }
        return monument;
    }

    public static List<Monument> getNearestMonuments(double minLat, double maxLat, double minLong, double maxLong){
        List<Monument> monumentList = new ArrayList<Monument>();

        String queryString = "SELECT ?place WHERE { " +
                "   ?place a <" + CidocSchema.E53_PLACE.getURI() + "> ." +
                "   ?place <" + GeoSchema.LAT.getURI() + "> ?lat ." +
                "   ?place <" + GeoSchema.LONG.getURI() + "> ?lon ." +
                " FILTER (" + "?lat >= "+ minLat + " && ?lat <= " + maxLat + " && ?lon >= "  + minLong + "&&  ?lon <= " + maxLong +" )" +
                "}";
        System.out.println("getNearestMonuments" + queryString);

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try{
            ResultSet results = qexec.execSelect();
            for (; results.hasNext(); ){
                QuerySolution soln = results.nextSolution();
                Resource place = (Resource) soln.get("?place");
                monumentList.add(getMonumentLocateAt(place.getURI()));
            }
        } finally {
            qexec.close();
        }
        return monumentList;
    }

    private static Monument getMonumentLocateAt (String placeUri) {
        Monument monument = null;
        String queryString = "SELECT ?X WHERE { " +
                "   ?X a <" + CidocSchema.E22_MAN_MADE_OBJECT.getURI() + "> ." +
                "   ?X <" + CidocSchema.P55_HAS_CURRENT_LOCATION.getURI() + "> <" + placeUri + ">" +
                "}";
        System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                System.out.println(soln);
                monument = getMonument(soln.get("?X").asResource());
            }
        } finally {
            qexec.close();
        }
        return monument;
    }

    public static String getImage(String representedUri){
        String imageUri = "";
        String queryString = "SELECT ?X WHERE { " +
                "   ?X a <" + CidocSchema.E38_IMAGE.getURI() + "> ." +
                "   ?X <" + CidocSchema.P138_REPRESENTS.getURI() + "> <" + representedUri + ">" +
                "}";
        LOG.log(Level.INFO, "Getting image with query: " + queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                imageUri = soln.get("?X").toString();
            }
        }finally {
            qexec.close();
        }
        return imageUri;

    }

    public static void main(String[] args){
//        Monument monument = MonumentService.getMonument(1l);
//        List<Warning> warnings = MonumentService.getWarnings(monument);
//        List<Post> posts = MonumentService.getPosts(monument);
//        TemperatureData temp = MonumentService.getTemp();
//        createMonument(monument);
//        WarningDao.createWarning(warnings.get(0), monument);
//        PostDao.createPost(posts.get(0), monument);
//        TemperatureDao.addTemperatureData(temp, monument);
//
//        OutputStream outFile = null;
//        try {
//            outFile = new FileOutputStream("/home/nerisa/codehome/project/java/thesis/src/main/resources/output2.rdfs", false);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        RDFDataMgr.write(outFile, model.getBaseModel(), RDFFormat.RDFXML_PLAIN);

//        getMonument(1l);

//        createMonument(new Monument(400l,"Sud Paris", "me", "Hellooooo", "https://firebasestorage.googleapis.com/v0/b/custodian-3e7c1.appspot.com/o/images%2Fbc344dff-fff6-4f5f-a6ca-866d78fd7744?alt=media&token=6012ef75-5abb-4d70-8dcc-40fdb9ae1251", 48.624061, 2.444167));
//        createMonument(new Monument(200l, "rue Charles Fourier", "me", "Hellooooo", "https://firebasestorage.googleapis.com/v0/b/custodian-3e7c1.appspot.com/o/images%2Fdff48770-ce01-4e0b-8d6d-4c643e030c69?alt=media&token=e8a2abe8-630e-44c7-8d91-0324325730ed", 48.625082, 2.443458));
        List<HashMap<String,Double>> places = getAllMonumentLocations();
        System.out.println(places.size());
        for(HashMap object: places){
            System.out.println("??????");
            System.out.println(object.toString());
        }
    }

    public static List<HashMap<String, Double>> getAllMonumentLocations(){
        List<HashMap<String, Double>> places = new ArrayList();
        String queryString = "SELECT ?place WHERE { " +
                "   ?place a <" + CidocSchema.E53_PLACE.getURI() + "> .}";
        LOG.log(Level.INFO, "Getting monument locations with query: " + queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            for (; results.hasNext(); ) {
                HashMap<String,Double> place = new HashMap<String, Double>();
                QuerySolution soln = results.nextSolution();
                Resource placeResource = soln.get("?place").asResource();
                place.put("lat", placeResource.getProperty(GeoSchema.LAT).getDouble());
                place.put("long", placeResource.getProperty(GeoSchema.LONG).getDouble());
                places.add(place);
            }
        }finally {
            qexec.close();
        }
        return places;
    }
}
