package graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Edge;
import model.Node;

public class Graph {

    private Map<Integer, Node> nodes;
    private Map<Integer, List<Edge>> adjacencyList;

    public Graph() {
        nodes = new HashMap<>();
        adjacencyList = new HashMap<>();
    }

    public void addNode(Node node) {

        nodes.put(node.getId(), node);

        adjacencyList.putIfAbsent(
                node.getId(),
                new ArrayList<>()
        );
    }

    public void addEdge(Edge edge) {

        int sourceId = edge.getSource().getId();

        adjacencyList
                .get(sourceId)
                .add(edge);
    }

    public Node getNode(int id) {

        return nodes.get(id);
    }

    public List<Edge> getNeighbors(int nodeId) {

        return adjacencyList.getOrDefault(
                nodeId,
                new ArrayList<>()
        );
    }

    public void displayGraph() {

        for (Node node : nodes.values()) {

            System.out.print(
                    node.getName() + " -> "
            );

            List<Edge> edges =
                    adjacencyList.get(node.getId());

            for (Edge edge : edges) {

                System.out.print(
                        edge.getDestination().getName()
                                + " ("
                                + edge.getDistance()
                                + " km), "
                );
            }

            System.out.println();
        }
    }
}