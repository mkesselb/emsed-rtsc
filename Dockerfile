FROM openjdk:17-jdk-alpine
COPY target/emsed_rtsc-0.1.0.jar emsed_rtsc-0.1.0.jar
COPY application.properties application.properties
ENTRYPOINT ["java","-jar","/emsed_rtsc-0.1.0.jar"]
