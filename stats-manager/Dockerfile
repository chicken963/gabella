FROM openjdk:17-jdk-alpine
COPY target/stats-manager-*.jar stats-manager.jar
ENTRYPOINT ["java","-jar","/stats-manager.jar"]