package algorithm;

import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteResult;

import java.util.*;

public class DijkstraAlgorithm {

    public RouteResult findShortestPath(
            Graph graph,
            int sourceId,
            int destinationId) {

        Map<Integer, Double> distances = new HashMap<>();

        Map<Integer, Integer> previous = new HashMap<>();

        PriorityQueue<NodeDistance> priorityQueue =
                new PriorityQueue<>(
                        Comparator.comparingDouble(
                                NodeDistance::getDistance
                        )
                );

        // Initialize distances
        for (Node node : graph.getAllNodes()) {
            distances.put(node.getId(), Double.POSITIVE_INFINITY);
        }

        // Distance from source to itself is 0
        distances.put(sourceId, 0.0);

        priorityQueue.add(
                new NodeDistance(
                        sourceId,
                        0.0
                )
        );

        while (!priorityQueue.isEmpty()) {

            NodeDistance current =
                    priorityQueue.poll();

            int currentId =
                    current.getNodeId();

            double currentDistance =
                    current.getDistance();

            // Ignore outdated queue entries
            if (currentDistance >
                    distances.get(currentId)) {
                continue;
            }

            // Destination reached
            if (currentId == destinationId) {
                break;
            }

            // Check all neighboring roads
            for (Edge edge :
                    graph.getNeighbors(currentId)) {

                int neighborId =
                        edge.getDestination().getId();

                double newDistance =
                        currentDistance +
                        edge.getTravelTimeHours();

                // Found a shorter route
                if (newDistance <
                        distances.get(neighborId)) {

                    distances.put(
                            neighborId,
                            newDistance
                    );

                    previous.put(
                            neighborId,
                            currentId
                    );

                    priorityQueue.add(
                            new NodeDistance(
                                    neighborId,
                                    newDistance
                            )
                    );
                }
            }
        }

        // Build the final path
        List<Node> path =
                new ArrayList<>();

        Integer current =
                destinationId;

        if (!previous.containsKey(current)
                && current != sourceId) {

            return new RouteResult(
                    path,
                    Double.POSITIVE_INFINITY
            );
        }

        while (current != null) {

            path.add(
                    graph.getNode(current)
            );

            current =
                    previous.get(current);
        }

        Collections.reverse(path);

        return new RouteResult(
                path,
                distances.get(destinationId)
        );
    }

    private static class NodeDistance {

        private int nodeId;
        private double distance;

        public NodeDistance(
                int nodeId,
                double distance) {

            this.nodeId = nodeId;
            this.distance = distance;
        }

        public int getNodeId() {
            return nodeId;
        }

        public double getDistance() {
            return distance;
        }
    }
}