package traffic;

import graph.Graph;
import model.Edge;
import model.Node;
import model.TrafficLevel;

public class TrafficManager {

    // ==========================================
    // UPDATE A SINGLE DIRECTED ROAD
    // ==========================================

    public void updateRoadTraffic(
            Edge edge,
            TrafficLevel newTrafficLevel) {

        edge.updateTraffic(
                newTrafficLevel
        );
    }

    // ==========================================
    // UPDATE A SINGLE DIRECTED ROAD
    // USING NODE IDs
    // ==========================================

    public void updateRoadTraffic(
            Graph graph,
            int sourceId,
            int destinationId,
            TrafficLevel newTrafficLevel) {

        Node source =
                graph.getNode(sourceId);

        Node destination =
                graph.getNode(destinationId);

        if (source == null) {

            throw new IllegalArgumentException(
                    "Source node not found: "
                            + sourceId
            );
        }

        if (destination == null) {

            throw new IllegalArgumentException(
                    "Destination node not found: "
                            + destinationId
            );
        }

        for (Edge edge :
                graph.getNeighbors(sourceId)) {

            if (edge.getDestination()
                    .getId()
                    == destinationId) {

                edge.updateTraffic(
                        newTrafficLevel
                );

                return;
            }
        }

        throw new IllegalArgumentException(
                "Road not found from "
                        + source.getName()
                        + " to "
                        + destination.getName()
        );
    }

    // ==========================================
    // UPDATE BOTH DIRECTIONS OF A ROAD
    // ==========================================

    public void updateBidirectionalTraffic(
            Graph graph,
            int nodeA,
            int nodeB,
            TrafficLevel newTrafficLevel) {

        Node firstNode =
                graph.getNode(nodeA);

        Node secondNode =
                graph.getNode(nodeB);

        if (firstNode == null) {

            throw new IllegalArgumentException(
                    "Node not found: "
                            + nodeA
            );
        }

        if (secondNode == null) {

            throw new IllegalArgumentException(
                    "Node not found: "
                            + nodeB
            );
        }

        boolean firstDirectionFound =
                false;

        boolean secondDirectionFound =
                false;

        // ------------------------------------------
        // A -> B
        // ------------------------------------------

        for (Edge edge :
                graph.getNeighbors(nodeA)) {

            if (edge.getDestination()
                    .getId()
                    == nodeB) {

                edge.updateTraffic(
                        newTrafficLevel
                );

                firstDirectionFound =
                        true;

                break;
            }
        }

        // ------------------------------------------
        // B -> A
        // ------------------------------------------

        for (Edge edge :
                graph.getNeighbors(nodeB)) {

            if (edge.getDestination()
                    .getId()
                    == nodeA) {

                edge.updateTraffic(
                        newTrafficLevel
                );

                secondDirectionFound =
                        true;

                break;
            }
        }

        // ------------------------------------------
        // VALIDATE BOTH DIRECTIONS
        // ------------------------------------------

        if (!firstDirectionFound
                || !secondDirectionFound) {

            throw new IllegalArgumentException(
                    "Bidirectional road not found between "
                            + firstNode.getName()
                            + " and "
                            + secondNode.getName()
            );
        }
    }
}