# emsed-rtsc

Emergency Services Dashboard with Real-Time Search Capabilities (Spring Boot demo)

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
  - [ ] Implement unit tests for at least two core functionalities: incident logging and incident search

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
    - `(?)` using the index of the PDF
- Search Optimization
  - [x] Leverage ElasticSearch's capabilities to ensure that search queries are fast and yield accurate results
    - `(?)` Querying with a query like that (programmatically built):
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
  - [ ] Dockerize the application, including ElasticSearch, to make it portable and easy to deploy

