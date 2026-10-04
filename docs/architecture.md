# System Architecture

## Intelligent Route Optimization System

```mermaid
flowchart TD
    USER["User / Browser"]
    UI["Leaflet Web Interface"]
    SEARCH["OpenStreetMap Nominatim"]
    API["Spring Boot REST API"]
    ROUTE["Route Controller"]
    DIJKSTRA["Dijkstra Algorithm"]
    ASTAR["A* Algorithm"]
    TRAFFIC["Traffic Manager"]
    EDGE["Traffic-Aware Edge Cost"]
    OSM["OpenStreetMap PBF"]
    OSMREADER["Osmosis PBF Reader"]
    GRAPH["Directed Road Graph"]
    DB["PostgreSQL"]
    HISTORY["Route History"]
    SWAGGER["Swagger / OpenAPI"]
    DOCKER["Docker Compose"]

    USER --> UI
    UI --> SEARCH
    UI --> API
    API --> ROUTE
    ROUTE --> DIJKSTRA
    ROUTE --> ASTAR
    OSM --> OSMREADER
    OSMREADER --> GRAPH
    GRAPH --> DIJKSTRA
    GRAPH --> ASTAR
    TRAFFIC --> EDGE
    EDGE --> DIJKSTRA
    EDGE --> ASTAR
    ROUTE --> HISTORY
    HISTORY --> DB
    API --> SWAGGER
    DOCKER --> API
    DOCKER --> DB
    DIJKSTRA --> API
    ASTAR --> API
    API --> UI
```

## Request Flow

```text
User
  |
  v
Leaflet Web Interface
  |
  v
Spring Boot REST API
  |
  +-------------------+
  |                   |
  v                   v
Dijkstra              A*
  |                   |
  +---------+---------+
            |
            v
     Traffic-Aware
       Road Graph
            |
            v
       Route Response
            |
       +----+----+
       |         |
       v         v
    Leaflet   PostgreSQL
      Map     Route History
```

## Traffic Processing

```text
Base Road Speed
       |
       v
Traffic Level
       |
       v
Traffic Multiplier
       |
       +------------------+
       |                  |
       v                  v
Effective Speed     Adjusted Cost
       |                  |
       +--------+---------+
                |
                v
         Route Recalculation
```

## Deployment

```text
                 Docker Compose
                       |
          +------------+------------+
          |                         |
          v                         v
   Spring Boot App             PostgreSQL
      Port 8080                  Port 5432
          |
          v
     OSM Data Volume
          |
          v
      Routing Graph
```
