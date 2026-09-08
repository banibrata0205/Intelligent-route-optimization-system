import algorithm.AStarAlgorithm;
import algorithm.DijkstraAlgorithm;
import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteResult;

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

        graph.addEdge(
                new Edge(
                        college,
                        hospital,
                        5
                )
        );

        graph.addEdge(
                new Edge(
                        college,
                        railwayStation,
                        10
                )
        );

        graph.addEdge(
                new Edge(
                        hospital,
                        airport,
                        3
                )
        );

        graph.addEdge(
                new Edge(
                        railwayStation,
                        airport,
                        4
                )
        );


        // =========================================
        // DIJKSTRA ALGORITHM
        // =========================================

        DijkstraAlgorithm dijkstra =
                new DijkstraAlgorithm();

        RouteResult dijkstraResult =
                dijkstra.findShortestPath(
                        graph,
                        1,
                        4
                );


        // =========================================
        // A* ALGORITHM
        // =========================================

        AStarAlgorithm aStar =
                new AStarAlgorithm();

        RouteResult aStarResult =
                aStar.findShortestPath(
                        graph,
                        1,
                        4
                );


        // =========================================
        // DISPLAY RESULTS
        // =========================================

        System.out.println();
        System.out.println("================================");
        System.out.println("        ROUTE RESULTS");
        System.out.println("================================");


        // =========================================
        // DIJKSTRA RESULT
        // =========================================

        System.out.println();
        System.out.println("Dijkstra:");

        System.out.print("Route: ");

        for (int i = 0;
             i < dijkstraResult.getPath().size();
             i++) {

            System.out.print(
                    dijkstraResult
                            .getPath()
                            .get(i)
                            .getName()
            );

            if (i <
                    dijkstraResult
                            .getPath()
                            .size() - 1) {

                System.out.print(" -> ");
            }
        }

        System.out.println();

        System.out.println(
                "Distance: "
                        + dijkstraResult
                                .getTotalDistance()
                        + " km"
        );


        // =========================================
        // A* RESULT
        // =========================================

        System.out.println();
        System.out.println("A*:");

        System.out.print("Route: ");

        for (int i = 0;
             i < aStarResult.getPath().size();
             i++) {

            System.out.print(
                    aStarResult
                            .getPath()
                            .get(i)
                            .getName()
            );

            if (i <
                    aStarResult
                            .getPath()
                            .size() - 1) {

                System.out.print(" -> ");
            }
        }

        System.out.println();

        System.out.println(
                "Distance: "
                        + aStarResult
                                .getTotalDistance()
                        + " km"
        );

        System.out.println("================================");
    }
}