# FHIR API #

## Architecture ##

The applicaton has three layers for processing requests and communicating with the VistA REST API. The outer layer is dictated by [Hapi FHIR](http://hapifhir.io/), and is a [Resource Provider](http://hapifhir.io/doc_rest_server.html). The functionality of this layer is essentially to validate and translate any incoming parameters. This layer calls the service layer, which in turn calls the VistaData layer which communicates with the VistA REST API. Results obtained from VistA are then passed back to the service layer (as text or JSON depending on the API called) and parsed into FHIR objects. The FHIR objects are passed back to the Hapi FHIR resource provider and returned to the caller.

![Java API architecture](../images/java-architecture.png)

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

Windows
	
~~~~
gradlew.bat clean build

java -jar build/libs/healthconcourse-vista-fhir-api-1.0.0.jar
~~~~

Test the application by visiting [http://localhost:8080/api/metadata](http://localhost:8080/api/metadata)

## Docker ##

To run the application in a Docker container, first build the project as above, but do not execute the `java` command. Then issue these commands:

~~~~
docker build -t vista-fhir-api .

docker run --name fhir-api  --restart=always -p 8080:8080  -d vista-fhir-api
~~~~