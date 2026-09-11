import algorithm.AStarAlgorithm;
import algorithm.DijkstraAlgorithm;
import algorithm.RouteAnalyzer;
import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteComparison;
import model.RouteResult;
import model.TrafficLevel;

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
        // INITIAL TRAFFIC CONDITIONS
        // =========================================

        collegeToHospital.updateTraffic(
                TrafficLevel.HEAVY
        );

        collegeToRailway.updateTraffic(
                TrafficLevel.NORMAL
        );

        hospitalToAirport.updateTraffic(
                TrafficLevel.HEAVY
        );

        railwayToAirport.updateTraffic(
                TrafficLevel.NORMAL
        );


        // =========================================
        // ADD ROADS TO GRAPH
        // =========================================

        graph.addEdge(collegeToHospital);
        graph.addEdge(collegeToRailway);
        graph.addEdge(hospitalToAirport);
        graph.addEdge(railwayToAirport);


        // =========================================
        // CREATE ALGORITHMS
        // =========================================

        DijkstraAlgorithm dijkstra =
                new DijkstraAlgorithm();

        AStarAlgorithm aStar =
                new AStarAlgorithm();


        // =========================================
        // INITIAL ROUTE
        // =========================================

        RouteResult initialDijkstraResult =
                dijkstra.findShortestPath(
                        graph,
                        1,
                        4
                );

        RouteResult initialAStarResult =
                aStar.findShortestPath(
                        graph,
                        1,
                        4
                );


        // =========================================
        // INITIAL ROUTE RESULTS
        // =========================================

        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "      INITIAL ROUTE"
        );

        System.out.println(
                "================================"
        );

        System.out.println();

        System.out.println(
                "Dijkstra:"
        );

        System.out.print(
                "Route: "
        );

        printRoute(
                initialDijkstraResult
        );

        System.out.printf(
                "Travel Time: %.2f minutes%n",
                initialDijkstraResult
                        .getTotalTravelTimeMinutes()
        );

        System.out.println();

        System.out.println(
                "A*:"
        );

        System.out.print(
                "Route: "
        );

        printRoute(
                initialAStarResult
        );

        System.out.printf(
                "Travel Time: %.2f minutes%n",
                initialAStarResult
                        .getTotalTravelTimeMinutes()
        );


        // =========================================
        // DYNAMIC TRAFFIC UPDATE
        // =========================================

        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "     TRAFFIC UPDATE"
        );

        System.out.println(
                "================================"
        );

        System.out.println();

        System.out.println(
                "College -> Railway Station"
        );

        System.out.println(
                "Traffic: NORMAL -> HEAVY"
        );


        collegeToRailway.updateTraffic(
                TrafficLevel.HEAVY
        );


        // =========================================
        // RECALCULATE ROUTE
        // =========================================

        RouteResult updatedDijkstraResult =
                dijkstra.findShortestPath(
                        graph,
                        1,
                        4
                );

        RouteResult updatedAStarResult =
                aStar.findShortestPath(
                        graph,
                        1,
                        4
                );


        // =========================================
        // UPDATED ROUTE RESULTS
        // =========================================

        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "      UPDATED ROUTE"
        );

        System.out.println(
                "================================"
        );

        System.out.println();

        System.out.println(
                "Dijkstra:"
        );

        System.out.print(
                "Route: "
        );

        printRoute(
                updatedDijkstraResult
        );

        System.out.printf(
                "Travel Time: %.2f minutes%n",
                updatedDijkstraResult
                        .getTotalTravelTimeMinutes()
        );

        System.out.println();

        System.out.println(
                "A*:"
        );

        System.out.print(
                "Route: "
        );

        printRoute(
                updatedAStarResult
        );

        System.out.printf(
                "Travel Time: %.2f minutes%n",
                updatedAStarResult
                        .getTotalTravelTimeMinutes()
        );


        // =========================================
        // ROUTE COMPARISON AFTER UPDATE
        // =========================================

        RouteAnalyzer routeAnalyzer =
                new RouteAnalyzer();

        RouteComparison comparison =
                routeAnalyzer.compareRoutes(
                        graph,
                        1,
                        4,
                        updatedDijkstraResult
                );


        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "    UPDATED ROUTE ANALYSIS"
        );

        System.out.println(
                "================================"
        );

        System.out.println();

        System.out.println(
                "Selected Route:"
        );

        printRoute(
                comparison.getSelectedRoute()
        );

        System.out.printf(
                "Travel Time: %.2f minutes%n",
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
                "Travel Time: %.2f minutes%n",
                comparison
                        .getAlternativeRoute()
                        .getTotalTravelTimeMinutes()
        );

        System.out.printf(
                "Time Saved: %.2f minutes%n",
                comparison.getTimeSavedMinutes()
        );


        // =========================================
        // ROAD INFORMATION
        // =========================================

        System.out.println();

        System.out.println(
                "================================"
        );

        System.out.println(
                "      CURRENT ROAD STATUS"
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