package com.nerisa.datarepo.ontology;

import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;

import java.io.*;
import java.util.Iterator;

/**
 * Created by nerisa on 3/19/18.
 */
public class Connection {

    public static final String DATABASE = "/home/nerisa/codehome/project/java/thesis-datarepo/MyDatabases/test";
    public static OntModel model;
    public static Dataset dataset = TDBFactory.createDataset(DATABASE);

    public static OntModel getModel(){

        final OntModel cidocModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
        cidocModel.read( "/home/nerisa/codehome/project/java/thesis-datarepo/src/main/resources/cidoc_crm.rdfs" );

        final OntModel sosaModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
        sosaModel.read("/home/nerisa/codehome/project/java/thesis-datarepo/src/main/resources/sosa.ttl");

        final OntModel qudtModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
        qudtModel.read("/home/nerisa/codehome/project/java/thesis-datarepo/src/main/resources/qudt.owl");

        Model model = dataset.getDefaultModel();

        final OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF, model);
        ontModel.addSubModel(cidocModel);
        ontModel.addSubModel(qudtModel);
        ontModel.addSubModel(sosaModel);

        return ontModel;
    }

    public static void closeConnections(){
        if(model != null){model.close();}
        if(dataset != null) {dataset.end();}
    }

    public static void openDataSetForRead(){
        dataset.begin(ReadWrite.READ);
    }

    public static void openDataSetForWrite(){
        dataset.begin(ReadWrite.WRITE);
    }

//    public static void main(String[] args) {
//        String SOURCE = "http://www.cidoc-crm.org/cidoc-crm/";
//        String NS = SOURCE + "#";
//        OntModel model = ModelFactory.createOntologyModel();
//        InputStream file = FileManager.get().open("/home/nerisa/codehome/project/java/thesis/src/main/resources/cidoc_crm.rdfs");
//        model.read(file, null);
//        OntClass class2 = model.getOntClass(SOURCE + "E3_Condition_State");
//        System.out.println(class2 == null);
//        System.out.println(class2.getSuperClass().toString());
//        Individual individual = class2.createIndividual(SOURCE + "conditionState");
//        Iterator<OntProperty> i = class2.listDeclaredProperties();
//        OntProperty property = model.getOntProperty(SOURCE+ "P3_has_note");
//        System.out.println(property == null);
//        while (i.hasNext()){
//            OntProperty o = i.next();
//            System.out.println(o.getURI());
//
//        }
//        individual.setPropertyValue(property,model.createTypedLiteral("Hello"));
//        OutputStream outFile = null;
//        try {
//            outFile = new FileOutputStream("/home/nerisa/codehome/project/java/thesis/src/main/resources/output.rdfs", false);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        RDFDataMgr.write(outFile, model,RDFFormat.RDFXML_PLAIN);

//        String directory = "MyDatabases/Dataset1";
//        Dataset dataset = TDBFactory.createDataset(directory);
//        dataset.begin( ReadWrite.WRITE );
//        Model model1 = null;
//        try
//        {
//            model1 = dataset.getNamedModel( SOURCE );
//            FileManager.get().readModel( model1, "/home/nerisa/codehome/project/java/thesis/src/main/resources/cidoc_crm.rdfs" );
//            dataset.commit();
//        }
//        finally
//        {
//            dataset.end();
//        }


//        dataset.begin(ReadWrite.WRITE);
//        String queryString =
//                "PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/> " +
//                        "INSERT DATA {" +
//                        "<http://example/thing9> a crm:E24_Physical_Man-Made_Thing}";
//        try {
//            UpdateRequest qExec = UpdateFactory.create(queryString);
//            UpdateProcessor processor = UpdateExecutionFactory.create(qExec, dataset);
//            processor.execute();
//            dataset.commit();
//        } finally {
//            dataset.end();
//        }
//
//        dataset.begin(ReadWrite.READ);
//        try {
//            Iterator<Quad> iter = dataset.asDatasetGraph().find();
//            while (iter.hasNext()) {
//                Quad quad = iter.next();
//                System.out.println(quad);
//            }
//        } finally {
//            dataset.end();
//        }

//        dataset.begin(ReadWrite.READ);
//        String queryString =
//                "PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>" +
//                        "SELECT ?x WHERE {" +
//                        "?x a crm:E24_Physical_Man-Made_Thing}";
//        try{
//            QueryExecution qExec = QueryExecutionFactory.create(queryString, dataset);
//            ResultSet rs = qExec.execSelect();
//            ResultSetFormatter.out(rs) ;
//        }finally {
//            dataset.end();
//        }
//    }

}

