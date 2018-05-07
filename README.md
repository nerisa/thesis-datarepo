This is the server side implementation for the Android app in the repo Custodian.

It has been deployed to http://206.189.172.86

The REST APIs are available at http://206.189.172.86:8080/thesis-datarepo_war

The data collected can be viewed at http://206.189.172.86:3332/ds/data

The SPARQL endpoint can be accessed at http://206.189.172.86:3332/ds by providing the SPARQL query with the param 'query' along with the URL. 

For e.g. http://206.189.172.86:3332/ds?query=SELECT?monument ?property ?object WHERE{?monument ?property ?object . ?monument a <http://www.cidoc-crm.org/cidoc-crm/E22_Man-Made_Object> . ?monument <http://www.cidoc-crm.org/cidoc-crm/P1_is_identified_by> ?name. ?name a <http://www.cidoc-crm.org/cidoc-crm/E41_Appellation> . ?name <http://www.cidoc-crm.org/cidoc-crm/P90_has_value> "Test monument"}
