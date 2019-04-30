# VistA REST Services#

## Architecture ##

The REST services use the RGNET web server.

![VistA REST Service architecture](../images/VistA-architecture.png)

## Configuration ##
There are two configuration items in the project, located in `java-api/src/main/resources/application.properties`: 

* vistaUrl - The base URL to the VistA REST API
* serverUrl - The URL to this application (required by Hapi FHIR)

Additionally, requests are served out of the `/api` directory in the application URL. This is set in the `HcFhirServlet` class.

## Build and Run ##

The project uses Gradle for compiling and running tests. There is no need to install Gradle, the Gradle wrapper included in the repository is sufficient to compile and test the code. The first time the wrapper is executed it will download the correct version of Gradle into the project directory. This step varies slightly by platform. Execute the commands below to build, test, and run the code.

MacOS or Linux
	
~~~~
./gradlew clean build

java -jar build/libs/healthconcourse-vista-fhir-api-1.0.0.jar
~~~~


Test the application by visiting [http://localhost:8080/api/metadata](http://localhost:8080/api/metadata)

To test the services run the following to obtain some ICN's (the example requests 3)

curl "http://localhost:8001/DHPPATICNALL?CNT=3"

1001949719V291733;1004751168V745102;1006145121V631417;

Then make a call for a resource e.g. vitals

curl "http://localhost:8001/DHPPATVITICN?ICN=1001949719V291733"

1001949719V291733^27113001|Weight|158.2|20110901201904-0500|V_500_120.5_33062^27113001|Weight|170.9|20141216091908-0500|V_500_120.5_33065^50373000|Height|68|20110901201904-0500|V_500_120.5_33061^50373000|Height|68|20141216091908-0500|V_500_120.5_33064^75367002|Blood pressure|144/96|20110901201904-0500|V_500_120.5_33063^75367002|Blood pressure|167/116|20141216091908-0500|V_500_120.5_33066



~~~~