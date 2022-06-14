FROM adoptopenjdk:8-jre-hotspot

COPY target/sellics-insightful-1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
