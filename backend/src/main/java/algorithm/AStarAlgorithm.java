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

        // =========================================
        // gScore
        // Actual travel time from source
        // to each node, measured in hours
        // =========================================

        Map<Integer, Double> gScore =
                new HashMap<>();


        // =========================================
        // fScore
        // Estimated total travel time
        // fScore = gScore + heuristic
        // =========================================

        Map<Integer, Double> fScore =
                new HashMap<>();


        // =========================================
        // previous
        // Used to reconstruct the route
        // =========================================

        Map<Integer, Integer> previous =
                new HashMap<>();


        // =========================================
        // PRIORITY QUEUE
        // Lowest estimated travel time first
        // =========================================

        PriorityQueue<NodeDistance> openSet =
                new PriorityQueue<>(
                        Comparator.comparingDouble(
                                NodeDistance::getScore
                        )
                );


        Node destination =
                graph.getNode(destinationId);


        // =========================================
        // INITIALIZE SCORES
        // =========================================

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


        // =========================================
        // STARTING NODE
        // =========================================

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


        // =========================================
        // A* SEARCH
        // =========================================

        while (!openSet.isEmpty()) {

            NodeDistance current =
                    openSet.poll();

            int currentId =
                    current.getNodeId();


            // =====================================
            // DESTINATION REACHED
            // =====================================

            if (currentId == destinationId) {
                break;
            }


            // =====================================
            // CHECK NEIGHBORING ROADS
            // =====================================

            for (Edge edge :
                    graph.getNeighbors(currentId)) {

                int neighborId =
                        edge.getDestination()
                                .getId();


                // =================================
                // ACTUAL TRAVEL TIME
                // =================================

                double tentativeGScore =
                        gScore.get(currentId)
                                + edge.getTravelTimeHours();


                // =================================
                // FOUND A BETTER ROUTE
                // =================================

                if (tentativeGScore <
                        gScore.get(neighborId)) {

                    // Remember previous node
                    previous.put(
                            neighborId,
                            currentId
                    );


                    // Update actual travel time
                    gScore.put(
                            neighborId,
                            tentativeGScore
                    );


                    // Calculate heuristic
                    double hScore =
                            heuristic(
                                    graph.getNode(neighborId),
                                    destination
                            );


                    // =================================
                    // f(n) = g(n) + h(n)
                    // =================================

                    double estimatedTotal =
                            tentativeGScore
                                    + hScore;


                    fScore.put(
                            neighborId,
                            estimatedTotal
                    );


                    // Add to priority queue
                    openSet.add(
                            new NodeDistance(
                                    neighborId,
                                    estimatedTotal
                            )
                    );
                }
            }
        }


        // =========================================
        // RECONSTRUCT PATH
        // =========================================

        List<Node> path =
                new ArrayList<>();

        Integer current =
                destinationId;


        // =========================================
        // NO ROUTE FOUND
        // =========================================

        if (!previous.containsKey(current)
                && current != sourceId) {

            return new RouteResult(
                    path,
                    Double.POSITIVE_INFINITY
            );
        }


        // =========================================
        // BUILD PATH BACKWARDS
        // =========================================

        while (current != null) {

            path.add(
                    graph.getNode(current)
            );

            current =
                    previous.get(current);
        }


        // Reverse the path
        Collections.reverse(path);


        // =========================================
        // RETURN RESULT
        // =========================================

        return new RouteResult(
                path,
                gScore.get(destinationId)
        );
    }


    // =============================================
    // HEURISTIC
    // =============================================
    //
    // Calculates an optimistic estimate of the
    // remaining travel time.
    //
    // Haversine distance → kilometers
    // Optimistic speed → 60 km/h
    // Result → hours
    //
    // =============================================

    private double heuristic(
            Node current,
            Node destination) {

        double distanceKm =
                GeoUtils.calculateDistance(
                        current,
                        destination
                );


        // Optimistic speed.
        // We assume the vehicle can travel at
        // 60 km/h for the heuristic.

        double optimisticSpeedKmh =
                60.0;


        // Convert distance to estimated hours

        return distanceKm /
                optimisticSpeedKmh;
    }


    // =============================================
    // NODE + SCORE
    // =============================================

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