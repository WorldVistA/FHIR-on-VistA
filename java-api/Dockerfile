# Multistage build to decrease size
# https://stackoverflow.com/questions/40958062/how-to-reduce-my-java-gradle-docker-image-size
# First Stage build
FROM openjdk:8-jdk-alpine as build
WORKDIR /fhir
COPY . /fhir
RUN /fhir/gradlew clean build

# Second Stage Run
FROM openjdk:8-jdk-alpine
ARG env
ENV profile=${env}
ENV VISTAURL='http://vista:9080/'
RUN echo "oh $profile"
EXPOSE 8080
WORKDIR /fhir
COPY --from=build /fhir/build/libs/healthconcourse-vista-fhir-api-*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=${profile}","-jar","app.jar"]

# For remote debugging in Eclipse
# https://stackoverflow.com/questions/31070671/debug-spring-boot-in-docker
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y","-Dspring.profiles.active=${profile}","-jar","app.jar"]
