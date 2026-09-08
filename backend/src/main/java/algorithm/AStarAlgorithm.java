package algorithm;

import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteResult;

import java.util.*;

public class AStarAlgorithm {

    public RouteResult findShortestPath(
            Graph graph,
            int sourceId,
            int destinationId) {

        Map<Integer, Double> gScore =
                new HashMap<>();

        Map<Integer, Double> fScore =
                new HashMap<>();

        Map<Integer, Integer> previous =
                new HashMap<>();

        PriorityQueue<NodeDistance> openSet =
                new PriorityQueue<>(
                        Comparator.comparingDouble(
                                NodeDistance::getScore
                        )
                );

        Node destination =
                graph.getNode(destinationId);

        // Initialize scores
        for (Node node : graph.getAllNodes()) {

            gScore.put(
                    node.getId(),
                    Double.POSITIVE_INFINITY
            );

            fScore.put(
                    node.getId(),
                    Double.POSITIVE_INFINITY
            );
        }

        // Starting node
        gScore.put(sourceId, 0.0);

        fScore.put(
                sourceId,
                heuristic(
                        graph.getNode(sourceId),
                        destination
                )
        );

        openSet.add(
                new NodeDistance(
                        sourceId,
                        fScore.get(sourceId)
                )
        );

        while (!openSet.isEmpty()) {

            NodeDistance current =
                    openSet.poll();

            int currentId =
                    current.getNodeId();

            // Destination reached
            if (currentId == destinationId) {
                break;
            }

            for (Edge edge :
                    graph.getNeighbors(currentId)) {

                int neighborId =
                        edge.getDestination().getId();

                double tentativeGScore =
                        gScore.get(currentId)
                        + edge.getDistance();

                if (tentativeGScore <
                        gScore.get(neighborId)) {

                    previous.put(
                            neighborId,
                            currentId
                    );

                    gScore.put(
                            neighborId,
                            tentativeGScore
                    );

                    double estimatedTotal =
                            tentativeGScore
                            + heuristic(
                                    graph.getNode(neighborId),
                                    destination
                            );

                    fScore.put(
                            neighborId,
                            estimatedTotal
                    );

                    openSet.add(
                            new NodeDistance(
                                    neighborId,
                                    estimatedTotal
                            )
                    );
                }
            }
        }

        // Reconstruct path
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
                gScore.get(destinationId)
        );
    }

    private double heuristic(
            Node current,
            Node destination) {

        double latitudeDifference =
                current.getLatitude()
                - destination.getLatitude();

        double longitudeDifference =
                current.getLongitude()
                - destination.getLongitude();

        return Math.sqrt(
                latitudeDifference
                        * latitudeDifference
                +
                longitudeDifference
                        * longitudeDifference
        );
    }

    private static class NodeDistance {

        private int nodeId;
        private double score;

        public NodeDistance(
                int nodeId,
                double score) {

            this.nodeId = nodeId;
            this.score = score;
        }

        public int getNodeId() {
            return nodeId;
        }

        public double getScore() {
            return score;
        }
    }
}