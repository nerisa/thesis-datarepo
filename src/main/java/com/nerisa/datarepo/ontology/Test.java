package com.nerisa.datarepo.ontology;

import com.nerisa.datarepo.utils.Constant;
import com.nerisa.datarepo.utils.Utility;
import org.apache.jena.base.Sys;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.base.file.Location;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by nerisa on 3/26/18.
 */
public class Test {

    final static String CIDOCNS = "http://www.cidoc-crm.org/cidoc-crm/";
    final static String QUDT = "http://qudt.org/schema/qudt#";

    public static void main(String[] args){
        /**
        testTripleStoreWithOnt();

        Dataset dataset = TDBFactory.createDataset("MyDatabases/Dataset2");
        dataset.begin(ReadWrite.READ);
        String queryString =
                "PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>" +
                        "SELECT ?x WHERE {" +
                        "?x a crm:E22_Man-Made_Object}";
        try{
            QueryExecution qExec = QueryExecutionFactory.create(queryString, dataset);
            ResultSet rs = qExec.execSelect();
            ResultSetFormatter.out(rs) ;
        }finally {
            dataset.end();
        }
         **/

//        testCode();

        System.out.println(Utility.getMonumentChildResourceId(Constant.BASE_URI + "/3" + Constant.WARNING_BASE_URI + "/4"));
    }


    private static void testTripleStoreWithOnt() {
        final String CIDOCNS = "http://www.cidoc-crm.org/cidoc-crm/";
        OntModelSpec ontModelSpec = OntModelSpec.OWL_MEM;
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        ontModelSpec.setReasoner(reasoner);


        String dataFolder = "MyDatabases/Dataset2";
        Dataset dataset = TDBFactory.createDataset(dataFolder);
        Model model = dataset.getDefaultModel();

        OntModel ontModel = ModelFactory.createOntologyModel(ontModelSpec, model);
        OntClass manMadeObject = CidocSchema.E22_MAN_MADE_OBJECT;
        Individual monumnet = ontModel.createIndividual("http://com.nerisa.thesis/1.0#monument", manMadeObject);
//        ontModel.commit();

        OutputStream outFile = null;
        try {
            outFile = new FileOutputStream("/home/nerisa/codehome/project/java/thesis/src/main/resources/output2.rdfs", false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RDFDataMgr.write(outFile, ontModel, RDFFormat.RDFXML_PLAIN);


    }

    public static void testCode(){

        final OntModel cidocModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
        cidocModel.read( "/home/nerisa/codehome/project/java/thesis/src/main/resources/cidoc_crm.rdfs" );

        // Create an OntModel that imports the cidocModel, and give it inference support.
        Dataset dataset = TDBFactory.createDataset("MyDatabases/test");
        Model model = dataset.getDefaultModel();

        final OntModel qudtModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
        qudtModel.read("/home/nerisa/codehome/project/java/thesis/src/main/resources/qudt.owl");

        final OntModel unitModel = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM );
        unitModel.read("/home/nerisa/codehome/project/java/thesis/src/main/resources/unit.rdfs");

        final OntModel model2 = ModelFactory.createOntologyModel( OntModelSpec.RDFS_MEM_RDFS_INF, model);
        model2.addSubModel( cidocModel );
        model2.addSubModel(qudtModel);
        model2.addSubModel(unitModel);

        final Property ontClass = model2.getProperty(QUDT + "unit");
        System.out.println(ontClass.getURI());

        // Retrieve a class from the OntModel and shows its subclasses.
        final OntClass e5_event = model2.getOntClass( CIDOCNS+"E41_Appellation" );
        System.out.println( "Subclasses of E5_Event:" );
        for ( final ExtendedIterator<OntClass> it = e5_event.listSubClasses(); it.hasNext() ;) {
            System.out.println( "\t* "+it.next() );
        }

//        Individual nameResource = model2.createIndividual("http://com.nerisa.thesis/1.0#monument", e5_event);
//        OntProperty property = model2.getOntProperty(CIDOCNS + "P90_has_value");
//        nameResource.addLiteral(property, "hello");
//
//        model2.commit();
//
//        OutputStream outFile = null;
//        try {
//            outFile = new FileOutputStream("/home/nerisa/codehome/project/java/thesis/src/main/resources/output-test.rdfs", false);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        RDFDataMgr.write(outFile, model2.getBaseModel(), RDFFormat.RDFXML_PLAIN);
    }
}
