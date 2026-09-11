import algorithm.AStarAlgorithm;
import algorithm.DijkstraAlgorithm;
import algorithm.RouteAnalyzer;
import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteComparison;
import model.RouteResult;
import model.TrafficLevel;
import traffic.TrafficManager;

public class Main {

    public static void main(String[] args) {

        // =========================================
        // CREATE NODES
        // =========================================

        Node college = new Node(
                1,
                "College",
                22.5726,
                88.3639
        );

        Node hospital = new Node(
                2,
                "Hospital",
                22.5750,
                88.3680
        );

        Node railwayStation = new Node(
                3,
                "Railway Station",
                22.5700,
                88.3750
        );

        Node airport = new Node(
                4,
                "Airport",
                22.6500,
                88.4460
        );


        // =========================================
        // CREATE GRAPH
        // =========================================

        Graph graph = new Graph();

        graph.addNode(college);
        graph.addNode(hospital);
        graph.addNode(railwayStation);
        graph.addNode(airport);


        // =========================================
        // CREATE ROADS
        // =========================================

        Edge collegeToHospital =
                new Edge(
                        college,
                        hospital,
                        5,
                        60
                );

        Edge collegeToRailway =
                new Edge(
                        college,
                        railwayStation,
                        10,
                        60
                );

        Edge hospitalToAirport =
                new Edge(
                        hospital,
                        airport,
                        3,
                        60
                );

        Edge railwayToAirport =
                new Edge(
                        railwayStation,
                        airport,
                        4,
                        60
                );


        // =========================================
        // CREATE TRAFFIC MANAGER
        // =========================================

        TrafficManager trafficManager =
                new TrafficManager();


        // =========================================
        // INITIAL TRAFFIC
        // =========================================

        trafficManager.updateRoadTraffic(
                collegeToHospital,
                TrafficLevel.HEAVY
        );

        trafficManager.updateRoadTraffic(
                collegeToRailway,
                TrafficLevel.NORMAL
        );

        trafficManager.updateRoadTraffic(
                hospitalToAirport,
                TrafficLevel.HEAVY
        );

        trafficManager.updateRoadTraffic(
                railwayToAirport,
                TrafficLevel.NORMAL
        );


        // =========================================
        // ADD ROADS
        // =========================================

        graph.addEdge(collegeToHospital);
        graph.addEdge(collegeToRailway);
        graph.addEdge(hospitalToAirport);
        graph.addEdge(railwayToAirport);


        // =========================================
        // CREATE ROUTING ALGORITHMS
        // =========================================

        DijkstraAlgorithm dijkstra =
                new DijkstraAlgorithm();

        AStarAlgorithm aStar =
                new AStarAlgorithm();


        // =========================================
        // INITIAL ROUTE
        // =========================================

        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "       TRAFFIC SIMULATION"
        );

        System.out.println(
                "================================"
        );


        System.out.println();

        System.out.println(
                "Initial Traffic:"
        );

        System.out.println(
                "College -> Railway Station = NORMAL"
        );


        displayRoutes(
                graph,
                dijkstra,
                aStar
        );


        // =========================================
        // UPDATE 1: HEAVY
        // =========================================

        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "       TRAFFIC UPDATE 1"
        );

        System.out.println(
                "================================"
        );

        System.out.println();

        System.out.println(
                "College -> Railway Station:"
        );

        System.out.println(
                "NORMAL -> HEAVY"
        );


        trafficManager.updateRoadTraffic(
                collegeToRailway,
                TrafficLevel.HEAVY
        );


        displayRoutes(
                graph,
                dijkstra,
                aStar
        );


        // =========================================
        // UPDATE 2: SEVERE
        // =========================================

        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "       TRAFFIC UPDATE 2"
        );

        System.out.println(
                "================================"
        );

        System.out.println();

        System.out.println(
                "College -> Railway Station:"
        );

        System.out.println(
                "HEAVY -> SEVERE"
        );


        trafficManager.updateRoadTraffic(
                collegeToRailway,
                TrafficLevel.SEVERE
        );


        displayRoutes(
                graph,
                dijkstra,
                aStar
        );


        // =========================================
        // UPDATE 3: NORMAL
        // =========================================

        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "       TRAFFIC UPDATE 3"
        );

        System.out.println(
                "================================"
        );

        System.out.println();

        System.out.println(
                "College -> Railway Station:"
        );

        System.out.println(
                "SEVERE -> NORMAL"
        );


        trafficManager.updateRoadTraffic(
                collegeToRailway,
                TrafficLevel.NORMAL
        );


        displayRoutes(
                graph,
                dijkstra,
                aStar
        );


        // =========================================
        // FINAL ROAD STATUS
        // =========================================

        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "       FINAL ROAD STATUS"
        );

        System.out.println(
                "================================"
        );

        System.out.println(
                collegeToHospital
        );

        System.out.println(
                collegeToRailway
        );

        System.out.println(
                hospitalToAirport
        );

        System.out.println(
                railwayToAirport
        );

        System.out.println(
                "================================"
        );
    }


    // =========================================
    // DISPLAY CURRENT ROUTES
    // =========================================

    private static void displayRoutes(
            Graph graph,
            DijkstraAlgorithm dijkstra,
            AStarAlgorithm aStar) {

        RouteResult dijkstraResult =
                dijkstra.findShortestPath(
                        graph,
                        1,
                        4
                );

        RouteResult aStarResult =
                aStar.findShortestPath(
                        graph,
                        1,
                        4
                );


        // =========================================
        // DIJKSTRA
        // =========================================

        System.out.println();

        System.out.println(
                "Dijkstra:"
        );

        System.out.print(
                "Route: "
        );

        printRoute(
                dijkstraResult
        );

        System.out.printf(
                "Travel Time: %.2f minutes%n",
                dijkstraResult
                        .getTotalTravelTimeMinutes()
        );


        // =========================================
        // A*
        // =========================================

        System.out.println();

        System.out.println(
                "A*:"
        );

        System.out.print(
                "Route: "
        );

        printRoute(
                aStarResult
        );

        System.out.printf(
                "Travel Time: %.2f minutes%n",
                aStarResult
                        .getTotalTravelTimeMinutes()
        );


        // =========================================
        // ROUTE COMPARISON
        // =========================================

        RouteAnalyzer routeAnalyzer =
                new RouteAnalyzer();

        RouteComparison comparison =
                routeAnalyzer.compareRoutes(
                        graph,
                        1,
                        4,
                        dijkstraResult
                );


        System.out.println();

        System.out.println(
                "Selected Route:"
        );

        printRoute(
                comparison.getSelectedRoute()
        );

        System.out.printf(
                "Time: %.2f minutes%n",
                comparison
                        .getSelectedRoute()
                        .getTotalTravelTimeMinutes()
        );

        System.out.println();

        System.out.println(
                "Alternative Route:"
        );

        printRoute(
                comparison.getAlternativeRoute()
        );

        System.out.printf(
                "Time: %.2f minutes%n",
                comparison
                        .getAlternativeRoute()
                        .getTotalTravelTimeMinutes()
        );
    }


    // =========================================
    // PRINT ROUTE
    // =========================================

    private static void printRoute(
            RouteResult result) {

        for (int i = 0;
             i < result.getPath().size();
             i++) {

            System.out.print(
                    result
                            .getPath()
                            .get(i)
                            .getName()
            );

            if (i <
                    result.getPath().size() - 1) {

                System.out.print(
                        " -> "
                );
            }
        }

        System.out.println();
    }
}