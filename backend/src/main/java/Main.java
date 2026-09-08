import graph.Graph;
import model.Edge;
import model.Node;

public class Main {

    public static void main(String[] args) {

        // Create locations
        Node A = new Node(1, "A");
        Node B = new Node(2, "B");
        Node C = new Node(3, "C");
        Node D = new Node(4, "D");

        // Create graph
        Graph graph = new Graph();

        // Add locations to graph
        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);

        // Add roads
        graph.addEdge(new Edge(A, B, 5));
        graph.addEdge(new Edge(A, C, 10));
        graph.addEdge(new Edge(B, D, 3));
        graph.addEdge(new Edge(C, D, 4));

        // Display road network
        graph.displayGraph();
    }
}