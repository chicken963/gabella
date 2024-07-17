FROM openjdk:17-jdk-alpine
COPY target/stats-generator-*.jar stats-generator.jar
ENTRYPOINT ["java","-jar","/stats-generator.jar"]