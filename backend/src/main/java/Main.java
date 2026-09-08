import algorithm.DijkstraAlgorithm;
import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteResult;

public class Main {

    public static void main(String[] args) {

        // Create locations
        Node A = new Node(1, "College");
        Node B = new Node(2, "Hospital");
        Node C = new Node(3, "Railway Station");
        Node D = new Node(4, "Airport");

        // Create graph
        Graph graph = new Graph();

        // Add locations
        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);

        // Add roads
        graph.addEdge(new Edge(A, B, 5));
        graph.addEdge(new Edge(A, C, 10));
        graph.addEdge(new Edge(B, D, 3));
        graph.addEdge(new Edge(C, D, 4));

        // Create Dijkstra algorithm
        DijkstraAlgorithm dijkstra =
                new DijkstraAlgorithm();

        // Find shortest route A → D
        RouteResult result =
                dijkstra.findShortestPath(
                        graph,
                        1,
                        4
                );

        // Display route
        System.out.println();
        System.out.println("Shortest Route:");

        for (Node node : result.getPath()) {

            System.out.print(
                    node.getName()
            );

            if (node !=
                    result.getPath()
                            .get(result.getPath().size() - 1)) {

                System.out.print(" -> ");
            }
        }

        System.out.println();

        System.out.println(
                "Total Distance: "
                        + result.getTotalDistance()
                        + " km"
        );
    }
}