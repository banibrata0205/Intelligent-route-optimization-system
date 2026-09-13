import java.util.Arrays;
import java.util.List;

import algorithm.AStarAlgorithm;
import algorithm.DijkstraAlgorithm;
import algorithm.MultiStopOptimizer;
import algorithm.RouteAnalyzer;
import graph.Graph;
import model.Edge;
import model.MultiStopRoute;
import model.Node;
import model.RouteComparison;
import model.RouteResult;
import model.TrafficLevel;
import traffic.TrafficManager;

public class Main {

    public static void main(String[] args) {

        // ==========================================
        // CREATE NODES
        // ==========================================

        Node college =
                new Node(
                        1,
                        "College",
                        22.5726,
                        88.3639
                );

        Node hospital =
                new Node(
                        2,
                        "Hospital",
                        22.5750,
                        88.3680
                );

        Node railwayStation =
                new Node(
                        3,
                        "Railway Station",
                        22.5700,
                        88.3750
                );

        Node airport =
                new Node(
                        4,
                        "Airport",
                        22.6500,
                        88.4460
                );

        // ==========================================
        // CREATE GRAPH
        // ==========================================

        Graph graph = new Graph();

        graph.addNode(college);
        graph.addNode(hospital);
        graph.addNode(railwayStation);
        graph.addNode(airport);

        // ==========================================
        // CREATE BIDIRECTIONAL ROADS
        // ==========================================

        // College <-> Hospital
        Edge collegeToHospital =
                new Edge(
                        college,
                        hospital,
                        5.0,
                        50.0
                );

        Edge hospitalToCollege =
                new Edge(
                        hospital,
                        college,
                        5.0,
                        50.0
                );

        // College <-> Railway Station
        Edge collegeToRailway =
                new Edge(
                        college,
                        railwayStation,
                        10.0,
                        50.0
                );

        Edge railwayToCollege =
                new Edge(
                        railwayStation,
                        college,
                        10.0,
                        50.0
                );

        // Hospital <-> Railway Station
        Edge hospitalToRailway =
                new Edge(
                        hospital,
                        railwayStation,
                        6.0,
                        50.0
                );

        Edge railwayToHospital =
                new Edge(
                        railwayStation,
                        hospital,
                        6.0,
                        50.0
                );

        // Hospital <-> Airport
        Edge hospitalToAirport =
                new Edge(
                        hospital,
                        airport,
                        3.0,
                        50.0
                );

        Edge airportToHospital =
                new Edge(
                        airport,
                        hospital,
                        3.0,
                        50.0
                );

        // Railway Station <-> Airport
        Edge railwayToAirport =
                new Edge(
                        railwayStation,
                        airport,
                        4.0,
                        50.0
                );

        Edge airportToRailway =
                new Edge(
                        airport,
                        railwayStation,
                        4.0,
                        50.0
                );

        // ==========================================
        // ADD ROADS TO GRAPH
        // ==========================================

        graph.addEdge(collegeToHospital);
        graph.addEdge(hospitalToCollege);

        graph.addEdge(collegeToRailway);
        graph.addEdge(railwayToCollege);

        graph.addEdge(hospitalToRailway);
        graph.addEdge(railwayToHospital);

        graph.addEdge(hospitalToAirport);
        graph.addEdge(airportToHospital);

        graph.addEdge(railwayToAirport);
        graph.addEdge(airportToRailway);

        // ==========================================
        // DISPLAY GRAPH
        // ==========================================

        System.out.println("===== ROAD NETWORK =====");

        graph.displayGraph();

        // ==========================================
        // CREATE ALGORITHMS
        // ==========================================

        DijkstraAlgorithm dijkstra =
                new DijkstraAlgorithm();

        AStarAlgorithm aStar =
                new AStarAlgorithm();

        // ==========================================
        // INITIAL ROUTING
        // ==========================================

        System.out.println();
        System.out.println(
                "===== INITIAL ROUTING ====="
        );

        RouteResult dijkstraRoute =
                dijkstra.findShortestPath(
                        graph,
                        college.getId(),
                        airport.getId()
                );

        RouteResult aStarRoute =
                aStar.findShortestPath(
                        graph,
                        college.getId(),
                        airport.getId()
                );

        displayRoutes(
                dijkstraRoute,
                aStarRoute
        );

        // ==========================================
        // ALTERNATIVE ROUTE ANALYSIS
        // ==========================================

        System.out.println();
        System.out.println(
                "===== ALTERNATIVE ROUTE ANALYSIS ====="
        );

        RouteAnalyzer routeAnalyzer =
                new RouteAnalyzer();

        RouteComparison comparison =
                routeAnalyzer.compareRoutes(
                        graph,
                        college.getId(),
                        airport.getId(),
                        dijkstraRoute
                );

        System.out.println(
                "Selected route:"
        );

        displayPath(
                comparison
                        .getSelectedRoute()
                        .getPath()
        );

        System.out.printf(
                "Travel time: %.2f minutes%n",
                comparison
                        .getSelectedRoute()
                        .getTotalTravelTimeMinutes()
        );

        System.out.println();

        System.out.println(
                "Alternative route:"
        );

        if (comparison
                .getAlternativeRoute()
                .getPath()
                .isEmpty()) {

            System.out.println(
                    "No alternative route available."
            );

        } else {

            displayPath(
                    comparison
                            .getAlternativeRoute()
                            .getPath()
            );

            System.out.printf(
                    "Travel time: %.2f minutes%n",
                    comparison
                            .getAlternativeRoute()
                            .getTotalTravelTimeMinutes()
            );

            System.out.printf(
                    "Time difference: %.2f minutes%n",
                    comparison.getTimeSavedMinutes()
            );
        }

        // ==========================================
        // CREATE TRAFFIC MANAGER
        // ==========================================

        TrafficManager trafficManager =
                new TrafficManager();

        // ==========================================
        // TRAFFIC UPDATE
        // COLLEGE -> RAILWAY
        // NORMAL -> HEAVY
        // ==========================================

        System.out.println();
        System.out.println(
                "===== TRAFFIC UPDATE: COLLEGE -> RAILWAY: NORMAL -> HEAVY ====="
        );

        trafficManager.updateRoadTraffic(
                collegeToRailway,
                TrafficLevel.HEAVY
        );

        dijkstraRoute =
                dijkstra.findShortestPath(
                        graph,
                        college.getId(),
                        airport.getId()
                );

        aStarRoute =
                aStar.findShortestPath(
                        graph,
                        college.getId(),
                        airport.getId()
                );

        displayRoutes(
                dijkstraRoute,
                aStarRoute
        );

        // ==========================================
        // TRAFFIC UPDATE
        // HEAVY -> SEVERE
        // ==========================================

        System.out.println();
        System.out.println(
                "===== TRAFFIC UPDATE: COLLEGE -> RAILWAY: HEAVY -> SEVERE ====="
        );

        trafficManager.updateRoadTraffic(
                collegeToRailway,
                TrafficLevel.SEVERE
        );

        dijkstraRoute =
                dijkstra.findShortestPath(
                        graph,
                        college.getId(),
                        airport.getId()
                );

        aStarRoute =
                aStar.findShortestPath(
                        graph,
                        college.getId(),
                        airport.getId()
                );

        displayRoutes(
                dijkstraRoute,
                aStarRoute
        );

        // ==========================================
        // TRAFFIC UPDATE
        // SEVERE -> NORMAL
        // ==========================================

        System.out.println();
        System.out.println(
                "===== TRAFFIC UPDATE: COLLEGE -> RAILWAY: SEVERE -> NORMAL ====="
        );

        trafficManager.updateRoadTraffic(
                collegeToRailway,
                TrafficLevel.NORMAL
        );

        dijkstraRoute =
                dijkstra.findShortestPath(
                        graph,
                        college.getId(),
                        airport.getId()
                );

        aStarRoute =
                aStar.findShortestPath(
                        graph,
                        college.getId(),
                        airport.getId()
                );

        displayRoutes(
                dijkstraRoute,
                aStarRoute
        );

        // ==========================================
        // FINAL ROAD STATUS
        // ==========================================

        System.out.println();
        System.out.println(
                "===== FINAL ROAD STATUS ====="
        );

        System.out.println(
                collegeToHospital
        );

        System.out.println(
                hospitalToCollege
        );

        System.out.println(
                collegeToRailway
        );

        System.out.println(
                railwayToCollege
        );

        System.out.println(
                hospitalToRailway
        );

        System.out.println(
                railwayToHospital
        );

        System.out.println(
                hospitalToAirport
        );

        System.out.println(
                airportToHospital
        );

        System.out.println(
                railwayToAirport
        );

        System.out.println(
                airportToRailway
        );

        // ==========================================
        // MULTI-STOP ROUTE OPTIMIZATION
        // ==========================================

        System.out.println();
        System.out.println(
                "===== MULTI-STOP ROUTE OPTIMIZATION ====="
        );

        MultiStopOptimizer optimizer =
                new MultiStopOptimizer(graph);

        List<Node> stops =
                Arrays.asList(
                        hospital,
                        railwayStation,
                        airport
                );

        MultiStopRoute multiStopRoute =
                optimizer.findOptimalRoute(
                        college,
                        stops
                );

        multiStopRoute.displayDetails();

        optimizer.displayRouteSegments();
    }

    // ==========================================
    // DISPLAY DIJKSTRA AND A* ROUTES
    // ==========================================

    private static void displayRoutes(
            RouteResult dijkstraRoute,
            RouteResult aStarRoute) {

        System.out.println();

        System.out.println(
                "Dijkstra route:"
        );

        displayPath(
                dijkstraRoute.getPath()
        );

        System.out.printf(
                "Travel time: %.2f minutes%n",
                dijkstraRoute
                        .getTotalTravelTimeMinutes()
        );

        System.out.println();

        System.out.println(
                "A* route:"
        );

        displayPath(
                aStarRoute.getPath()
        );

        System.out.printf(
                "Travel time: %.2f minutes%n",
                aStarRoute
                        .getTotalTravelTimeMinutes()
        );
    }

    // ==========================================
    // DISPLAY PATH
    // ==========================================

    private static void displayPath(
            List<Node> path) {

        if (path.isEmpty()) {

            System.out.println(
                    "No route found."
            );

            return;
        }

        for (int i = 0;
             i < path.size();
             i++) {

            System.out.print(
                    path.get(i).getName()
            );

            if (i < path.size() - 1) {

                System.out.print(
                        " -> "
                );
            }
        }

        System.out.println();
    }
}