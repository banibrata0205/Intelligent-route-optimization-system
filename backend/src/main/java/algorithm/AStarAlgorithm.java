package algorithm;

import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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

        // ==========================================
        // INITIALIZE SCORES
        // ==========================================

        for (Node node :
                graph.getAllNodes()) {

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
        gScore.put(
                sourceId,
                0.0
        );

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

        // ==========================================
        // A* SEARCH
        // ==========================================

        while (!openSet.isEmpty()) {

            NodeDistance current =
                    openSet.poll();

            int currentId =
                    current.getNodeId();

            double currentScore =
                    current.getScore();

            // Ignore stale priority-queue entries
            if (currentScore >
                    fScore.get(currentId)) {

                continue;
            }

            // Do NOT stop immediately when the
            // destination is reached.
            //
            // We continue processing the queue
            // so that better paths can still be
            // discovered.

            for (Edge edge :
                    graph.getNeighbors(
                            currentId
                    )) {

                int neighborId =
                        edge.getDestination()
                                .getId();

                // Current path cost + edge travel time
                double tentativeGScore =
                        gScore.get(currentId)
                                + edge.getTravelTimeHours();

                // Found a better route to neighbor
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

                    double heuristicScore =
                            heuristic(
                                    graph.getNode(
                                            neighborId
                                    ),
                                    destination
                            );

                    double estimatedTotal =
                            tentativeGScore
                                    + heuristicScore;

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

        // ==========================================
        // BUILD PATH
        // ==========================================

        List<Node> path =
                new ArrayList<>();

        Integer current =
                destinationId;

        // Destination is unreachable
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

        // ==========================================
        // RETURN RESULT
        // ==========================================

        return new RouteResult(
                path,
                gScore.get(destinationId)
        );
    }

    // ==========================================
    // HEURISTIC
    // ==========================================

    private double heuristic(
            Node current,
            Node destination) {

        double distanceKm =
                GeoUtils.calculateDistance(
                        current,
                        destination
                );

        /*
         * The roads in our current model have
         * a maximum speed of 50 km/h.
         *
         * Traffic only decreases the effective
         * speed:
         *
         * NORMAL   = 50.00 km/h
         * LIGHT    = 41.67 km/h
         * MODERATE = 33.33 km/h
         * HEAVY    = 25.00 km/h
         * SEVERE   = 16.67 km/h
         *
         * Therefore 50 km/h is the fastest
         * possible travel speed in our model.
         *
         * Distance / maximum speed gives an
         * optimistic lower bound on travel time.
         */

        double optimisticSpeedKmh =
                50.0;

        return distanceKm /
                optimisticSpeedKmh;
    }

    // ==========================================
    // PRIORITY QUEUE NODE
    // ==========================================

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