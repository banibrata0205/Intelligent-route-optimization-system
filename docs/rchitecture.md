@'
# System Architecture

## Intelligent Route Optimization System

```mermaid
flowchart TD

    USER["User / Browser"]

    UI["Leaflet Web Interface<br/>HTML + CSS + JavaScript"]

    SEARCH["OpenStreetMap Nominatim<br/>Place Search"]

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

    TRAFFIC --> EDGE
    EDGE --> DIJKSTRA
    EDGE --> ASTAR

    OSM --> OSMREADER
    OSMREADER --> GRAPH

    GRAPH --> DIJKSTRA
    GRAPH --> ASTAR

    ROUTE --> HISTORY
    HISTORY --> DB

    API --> SWAGGER

    DOCKER --> API
    DOCKER --> DB

    DIJKSTRA --> API
    ASTAR --> API

    API --> UI