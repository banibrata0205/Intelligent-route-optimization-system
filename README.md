@'
# Intelligent Route Optimization System

An intelligent geographic route optimization system built with Java, Spring Boot, OpenStreetMap, Dijkstra, A*, dynamic traffic simulation, PostgreSQL, Leaflet and Docker.

## Features

- OpenStreetMap road-network integration
- Real geographic routing for the Kolkata area
- Dijkstra shortest-path algorithm
- A* heuristic-based routing
- Dijkstra vs A* comparison
- Dynamic traffic levels:
  - NORMAL
  - LIGHT
  - MODERATE
  - HEAVY
  - SEVERE
- Traffic-adjusted edge cost and travel time
- Interactive Leaflet map
- Place-name search using OpenStreetMap Nominatim
- Route history using PostgreSQL
- REST APIs using Spring Boot
- Swagger/OpenAPI documentation
- Global API error handling
- JUnit tests
- Docker and Docker Compose support

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Backend | Spring Boot |
| API | REST |
| Algorithms | Dijkstra, A* |
| Map Data | OpenStreetMap |
| OSM Processing | Osmosis |
| Frontend | HTML, CSS, JavaScript, Leaflet |
| Database | PostgreSQL |
| Persistence | Spring Data JPA / Hibernate |
| API Documentation | Swagger / OpenAPI |
| Testing | JUnit |
| Build Tool | Maven |
| Containerization | Docker / Docker Compose |

## Architecture

```text
                  OpenStreetMap PBF
                         |
                         v
                   OSM PBF Reader
                         |
                         v
                    Road Graph
                         |
             +-----------+-----------+
             |                       |
             v                       v
         Dijkstra                    A*
             |                       |
             +-----------+-----------+
                         |
                         v
                Traffic-adjusted
                  Route Cost
                         |
                         v
                   Spring Boot
                    REST API
                         |
             +-----------+-----------+
             |                       |
             v                       v
        Leaflet UI              PostgreSQL
             |                   Route History
             +-----------+-----------+
                         |
                         v
                       Docker