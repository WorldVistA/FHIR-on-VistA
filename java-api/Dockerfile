FROM openjdk:8-jdk-alpine
ARG env
ENV profile=${env}
RUN echo "oh $profile"
ADD build/libs/healthconcourse-vista-fhir-api-*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=${profile}","-jar","app.jar"]