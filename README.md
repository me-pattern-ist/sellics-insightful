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
- Try running schema initialization of schema on startup in containerized postgres to verify setup. Ran multiple commands as shown below:
```shell
docker-compose up --build
docker ps
docker container exec -it <container-id> "bash"
bash-5.1# psql -U postgres
postgres=# \l
postgres=# \c search_engine_db
search_engine_db-# \d
search_engine_db-# \q
bash-5.1# exit
```

# Project Environment Used
- Java 1.8
- Maven 3.6
- Spring 5+
- Tomcat 9
- Docker and Docker Compose (App and Postgres)
