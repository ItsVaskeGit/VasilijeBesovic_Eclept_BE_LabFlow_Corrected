FROM openjdk:24-ea-17-jdk-slim
VOLUME /tmp
COPY target/VasilijeBesovic_Eclept_BE_LabFlow-0.0.1-SNAPSHOT.jar LabFlow.jar

ENTRYPOINT ["java", "-jar", "/LabFlow.jar"]