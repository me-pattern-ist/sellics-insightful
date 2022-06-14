# sellics-insightful

Consolidate information from csv data.

# Solution approach
- Using Spring batch to load data from s3 into postgres using spring batch asynchronously.
- Expose rest endpoints to monitor jobs and get consolidated information from database.
- With current trends, this could have been implemented using spark/flink in memory processing which are more expressive in coding style and concise.


# Thoughts while working
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
- Read about spring batch to make schema init execution happened at server startup.
- Try to model pojos to hold state.
- Thought to add aws config bean just to start.
- Read about spring batch to make connection with aws s3 and load data into database.
- Made data load on demand using rest apis.
```shell
curl -i -X POST http://localhost:7171/api/admin/jobs
curl -i -X GET http://localhost:7171/api/admin/jobs/<id-here>
```
- Added api to generate consolidate reports.
```shell
curl -i -X GET --location "http://localhost:7171/api/ranks/products"
curl -i -X GET --location "http://localhost:7171/api/ranks/products?serialNumber=B09LS4PB8P"
curl -i -X GET --location "http://localhost:7171/api/ranks/products?startDate=1635988126&endDate=1637199767&serialNumber=B09LS4PB8P"

curl -i -X GET --location "http://localhost:7171/api/keywords/products?keyword=2012"
curl -i -X GET --location "http://localhost:7171/api/keywords/products?keyword=2012&startDate=1637187600&endDate=1637199767"
```
- TODO Added some input validation to api, service layer and integration test.


# Project Environment Used
- Java 1.8
- Maven 3.6
- Spring 5+
- Tomcat 9
- Docker and Docker Compose (App and Postgres)
