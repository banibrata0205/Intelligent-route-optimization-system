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
        // Actual distance from source to each node
        // =========================================

        Map<Integer, Double> gScore =
                new HashMap<>();


        // =========================================
        // fScore
        // Estimated total distance
        // fScore = gScore + heuristic
        // =========================================

        Map<Integer, Double> fScore =
                new HashMap<>();


        // =========================================
        // previous
        // Used to reconstruct the final route
        // =========================================

        Map<Integer, Integer> previous =
                new HashMap<>();


        // =========================================
        // Priority Queue
        // Node with lowest fScore is processed first
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


                // Actual distance through current node
                double tentativeGScore =
                        gScore.get(currentId)
                                + edge.getDistance();


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


                    // Update actual distance
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


                    // f(n) = g(n) + h(n)
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


        // No route found
        if (!previous.containsKey(current)
                && current != sourceId) {

            return new RouteResult(
                    path,
                    Double.POSITIVE_INFINITY
            );
        }


        // Build path backwards
        while (current != null) {

            path.add(
                    graph.getNode(current)
            );

            current =
                    previous.get(current);
        }


        // Reverse path
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
    // HEURISTIC FUNCTION
    // =============================================
    //
    // Uses Haversine formula to calculate the
    // straight-line geographic distance in km.
    //
    // =============================================

    private double heuristic(
            Node current,
            Node destination) {

        return GeoUtils.calculateDistance(
                current,
                destination
        );
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