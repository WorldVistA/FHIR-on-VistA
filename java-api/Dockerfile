# Multistage build to decrease size
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
