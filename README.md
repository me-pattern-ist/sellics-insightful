# sellics-insightful

Consolidate information from csv data.

# Approach
- Setting up workspace with profiles to have checkstyle, spotbugs in place.
```shell
mvn clean package
mvn clean package -PCheckstyle
mvn clean package -PSpotbugs

```
- Thought to make application to use docker and docker-compose to avoid installing database locally and test application.


# Project Environment Used
- Java 1.8
- Maven 3.6
- Spring 5+
- Tomcat 9
- Docker and Docker Compose
- 