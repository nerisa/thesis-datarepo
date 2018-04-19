package com.nerisa.datarepo.ontology;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

/**
 * Vocabulary definitions from /home/nerisa/Desktop/sosa.owl
 * @author Auto-generated by schemagen on 26 Mar 2018 18:47
 */
public class SosaSchema {
    /** <p>The ontology model that holds the vocabulary terms</p> */
    private static final OntModel M_MODEL = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );

    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.w3.org/ns/sosa/";

    /** <p>The namespace of the vocabulary as a string</p>
     * @return namespace as String
     * @see #NS */
    public static String getURI() {return NS;}

    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = M_MODEL.createResource( NS );

    /** <p>Relation between an Actuation and the property of a FeatureOfInterest it is
     *  acting upon.</p>
     */
    public static final ObjectProperty ACTS_ON_PROPERTY = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/actsOnProperty" );

    /** <p>A relation between an Observation and the entity whose quality was observed,
     *  or between an Actuation and the entity whose property was modified, or between
     *  an act of Sampling and the entity that was sampled.</p>
     */
    public static final ObjectProperty HAS_FEATURE_OF_INTEREST = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/hasFeatureOfInterest" );

    /** <p>Relation linking an Observation or Actuation or act of Sampling and a Result
     *  or Sample.</p>
     */
    public static final ObjectProperty HAS_RESULT = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/hasResult" );

    /** <p>Relation between a FeatureOfInterest and the Sample used to represent it.</p> */
    public static final ObjectProperty HAS_SAMPLE = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/hasSample" );

    /** <p>Relation between a Platform and a Sensor, Actuator, Sampler, or Platform,
     *  hosted or mounted on it.</p>
     */
    public static final ObjectProperty HOSTS = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/hosts" );

    /** <p>Relation between an ActuatableProperty of a FeatureOfInterest and an Actuation
     *  changing its state.</p>
     */
    public static final ObjectProperty IS_ACTED_ON_BY = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/isActedOnBy" );

    /** <p>A relation between a FeatureOfInterest and an Observation about it, an Actuation
     *  acting on it, or an act of Sampling that sampled it.</p>
     */
    public static final ObjectProperty IS_FEATURE_OF_INTEREST_OF = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/isFeatureOfInterestOf" );

    /** <p>Relation between a Sensor, Actuator, Sampler, or Platform, and the Platform
     *  that it is mounted on or hosted by.</p>
     */
    public static final ObjectProperty IS_HOSTED_BY = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/isHostedBy" );

    /** <p>Relation between an ObservableProperty and the Sensor able to observe it.</p> */
    public static final ObjectProperty IS_OBSERVED_BY = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/isObservedBy" );

    /** <p>Relation linking a Result to the Observation or Actuation or act of Sampling
     *  that created or caused it.</p>
     */
    public static final ObjectProperty IS_RESULT_OF = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/isResultOf" );

    /** <p>Relation from a Sample to the FeatureOfInterest that it is intended to be
     *  representative of.</p>
     */
    public static final ObjectProperty IS_SAMPLE_OF = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/isSampleOf" );

    /** <p>Relation between an Actuator and the Actuation it has made.</p> */
    public static final ObjectProperty MADE_ACTUATION = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/madeActuation" );

    /** <p>Relation linking an Actuation to the Actuator that made that Actuation.</p> */
    public static final ObjectProperty MADE_BY_ACTUATOR = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/madeByActuator" );

    /** <p>Relation linking an act of Sampling to the Sampler (sampling device or entity)
     *  that made it.</p>
     */
    public static final ObjectProperty MADE_BY_SAMPLER = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/madeBySampler" );

    /** <p>Relation between an Observation and the Sensor which made the Observation.</p> */
    public static final ObjectProperty MADE_BY_SENSOR = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/madeBySensor" );

    /** <p>Relation between a Sensor and an Observation made by the Sensor.</p> */
    public static final ObjectProperty MADE_OBSERVATION = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/madeObservation" );

    /** <p>Relation between a Sampler (sampling device or entity) and the Sampling act
     *  it performed.</p>
     */
    public static final ObjectProperty MADE_SAMPLING = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/madeSampling" );

    /** <p>Relation linking an Observation to the property that was observed. The ObservableProperty
     *  should be a property of the FeatureOfInterest (linked by hasFeatureOfInterest)
     *  of this Observation.</p>
     */
    public static final ObjectProperty OBSERVED_PROPERTY = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/observedProperty" );

    /** <p>Relation between a Sensor and an ObservableProperty that it is capable of
     *  sensing.</p>
     */
    public static final ObjectProperty OBSERVES = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/observes" );

    /** <p>The time that the Result of an Observation, Actuation or Sampling applies
     *  to the FeatureOfInterest. Not necessarily the same as the resultTime. May
     *  be an Interval or an Instant, or some other compound TemporalEntity.</p>
     */
    public static final ObjectProperty PHENOMENON_TIME = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/phenomenonTime" );

    /** <p>A relation to link to a re-usable Procedure used in making an Observation,
     *  an Actuation, or a Sample, typically through a Sensor, Actuator or Sampler.</p>
     */
    public static final ObjectProperty USED_PROCEDURE = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/usedProcedure" );

    /** <p>The simple value of an Observation or Actuation or act of Sampling.</p> */
    public static final DatatypeProperty HAS_SIMPLE_RESULT = M_MODEL.createDatatypeProperty( "http://www.w3.org/ns/sosa/hasSimpleResult" );

    /** <p>The result time is the instant of time when the Observation, Actuation or
     *  Sampling activity was completed.</p>
     */
    public static final DatatypeProperty RESULT_TIME = M_MODEL.createDatatypeProperty( "http://www.w3.org/ns/sosa/resultTime" );

    /** <p>An actuatable quality (property, characteristic) of a FeatureOfInterest.</p> */
    public static final OntClass ACTUATABLE_PROPERTY = M_MODEL.createClass( "http://www.w3.org/ns/sosa/ActuatableProperty" );

    /** <p>An Actuation carries out an (Actuation) Procedure to change the state of the
     *  world using an Actuator.</p>
     */
    public static final OntClass ACTUATION = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Actuation" );

    /** <p>A device that is used by, or implements, an (Actuation) Procedure that changes
     *  the state of the world.</p>
     */
    public static final OntClass ACTUATOR = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Actuator" );

    /** <p>The thing whose property is being estimated or calculated in the course of
     *  an Observation to arrive at a Result or whose property is being manipulated
     *  by an Actuator, or which is being sampled or transformed in an act of Sampling.</p>
     */
    public static final OntClass FEATURE_OF_INTEREST = M_MODEL.createClass( "http://www.w3.org/ns/sosa/FeatureOfInterest" );

    /** <p>An observable quality (property, characteristic) of a FeatureOfInterest.</p> */
    public static final OntClass OBSERVABLE_PROPERTY = M_MODEL.createClass( "http://www.w3.org/ns/sosa/ObservableProperty" );

    /** <p>Act of carrying out an (Observation) Procedure to estimate or calculate a
     *  value of a property of a FeatureOfInterest. Links to a Sensor to describe
     *  what made the Observation and how; links to an ObservableProperty to describe
     *  what the result is an estimate of, and to a FeatureOfInterest to detail what
     *  that property was associated with.</p>
     */
    public static final OntClass OBSERVATION = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Observation" );

    /** <p>A Platform is an entity that hosts other entities, particularly Sensors, Actuators,
     *  Samplers, and other Platforms.</p>
     */
    public static final OntClass PLATFORM = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Platform" );

    /** <p>A workflow, protocol, plan, algorithm, or computational method specifying
     *  how to make an Observation, create a Sample, or make a change to the state
     *  of the world (via an Actuator). A Procedure is re-usable, and might be involved
     *  in many Observations, Samplings, or Actuations. It explains the steps to be
     *  carried out to arrive at reproducible results.</p>
     */
    public static final OntClass PROCEDURE = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Procedure" );

    /** <p>The Result of an Observation, Actuation, or act of Sampling. To store an observation's
     *  simple result value one can use the hasSimpleResult property.</p>
     */
    public static final OntClass RESULT = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Result" );

    /** <p>Feature which is intended to be representative of a FeatureOfInterest on which
     *  Observations may be made.Physical samples are sometimes known as 'specimens'.Samples
     *  are artifacts of an observational strategy, and have no significant function
     *  outside of their role in the observation process. The characteristics of the
     *  samples themselves are of little interest, except perhaps to the manager of
     *  a sampling campaign. A Sample is intended to sample some FatureOfInterest,
     *  so there is an expectation of at least one isSampleOf property. However, in
     *  some cases the identity, and even the exact type, of the sampled feature may
     *  not be known when observations are made using the sampling features.A Sample
     *  is the result from an act of Sampling.</p>
     */
    public static final OntClass SAMPLE = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Sample" );

    /** <p>A device that is used by, or implements, a Sampling Procedure to create or
     *  transform one or more samples.</p>
     */
    public static final OntClass SAMPLER = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Sampler" );

    /** <p>An act of Sampling carries out a sampling Procedure to create or transform
     *  one or more samples.</p>
     */
    public static final OntClass SAMPLING = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Sampling" );

    /** <p>Device, agent (including humans), or software (simulation) involved in, or
     *  implementing, a Procedure. Sensors respond to a stimulus, e.g., a change in
     *  the environment, or input data composed from the results of prior Observations,
     *  and generate a Result. Sensors can be hosted by Platforms.</p>
     */
    public static final OntClass SENSOR = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Sensor" );

    public static final Individual __ = M_MODEL.createIndividual( "http://www.w3.org/ns/sosa/", M_MODEL.createClass( "http://purl.org/vocommons/voaf#Vocabulary" ) );

}
