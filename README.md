# emsed-rtsc

Emergency Services Dashboard with Real-Time Search Capabilities (Spring Boot demo)

## Running the Application

Running the application consists of:
- download the `bootstrapping release version` from Github. This contains a ready-to-use `.jar` for bootstrapping the application.
- run docker-compose to create the Spring Boot & elastic-search container

```
docker-compose up --build
```

`Note:` after starting the first elastic-search container with the bootstrapping version, the application can be tested and re-built with:

```
.\mvnw.cmd clean package
```

This is not possible before as the startup application context needs a running elasticsearch (see the class `src\main\java\com\demo\emsed_rtsc\ElasticSearchConfig.java`).

## Specifications / Features

Checked features are implemented.

### General
- Spring Boot Setup
  - [x] Use Spring Boot for setting up the application
- ElasticSearch Integration
  - [x] Integrate ElasticSearch for real-time search capabilities
- Data Persistence
  - [x] Use Hibernate for data persistence. Sync this data to ElasticSearch for optimized searching
- Unit Testing
  - [x] Implement unit tests for at least two core functionalities: incident logging and incident search

Incidents
- Create and Log Incidents
  - [x] Implement a RESTful API endpoint to log emergency incidents
  - [x] Incidents should have attributes like incidentType, location, timestamp, and severityLevel
- Search Incidents        
  - [x] Create another API endpoint to perform searches based on parameters such as incidentType, location, and timestamp  
  - [x] Allow combination searches (e.g., all 'fire' incidents in a specific 'location')

ElasticSearch
- Data Indexing
  - [x] Use ElasticSearch to index the emergency incidents. The indexed data should be optimized for efficient querying
    - currently using the index-creation of the snippet in assignment.
    - [x] create index in initialization step -> should be handled by auto-creation in `Incident.java` elasticsearch `@Document`.
- Search Optimization
  - [x] Leverage ElasticSearch's capabilities to ensure that search queries are fast and yield accurate results
    - Querying with a query as below (programmatically built with `QueryBuilders` and `NativeQueryBuilder`, searching via `ElasticsearchOperations::search`):
    <details>
    <summary>Query</summary>

    ```
    {
      "query": {
          "bool": {
              "filter": [
                  {
                      "geo_distance": {
                          "location": {
                              "lat": 0.1,
                              "lon": 1.2
                          },
                          "distance": "5km"
                      }
                  }
              ],
              "must": [
                  {
                      "range": {
                          "timestamp": {
                              "gte": "2024-09-06T10:21:28.796+00:00",
                              "lte": "2024-09-08T10:21:28.796+00:00"
                          }
                      }
                  },
                  {
                      "terms": {
                          "incidentType": [
                              "FIRE",
                              "MEDICAL"
                          ]
                      }
                  },
                  {
                      "terms": {
                          "severityLevel": [
                              "MEDIUM",
                              "HIGH"
                          ]
                      }
                  }
              ]
          }
      }
    }
    ```
    <details>

Optional Task - Real-Time Dashboard
- WebSocket Integration
  - [ ] Integrate a WebSocket using Spring to provide real-time updates of incidents
- UI (Optional)
  - [ ] Implement a simple UI using Thymeleaf to visualize these real-time updates. This is for extra points and is not mandatory

Optional Task - Dockerization
- Docker Setup
  - [x] Dockerize the application, including ElasticSearch, to make it portable and easy to deploy

### Time spent:

- Spring tutorials: 10h
- Implementing functionality: 11.5h
