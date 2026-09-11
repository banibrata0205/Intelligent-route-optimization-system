package algorithm;

import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteComparison;
import model.RouteResult;

import java.util.ArrayList;
import java.util.List;

public class RouteAnalyzer {

    // =========================================
    // COMPARE ROUTES
    // =========================================

    public RouteComparison compareRoutes(
            Graph graph,
            int sourceId,
            int destinationId,
            RouteResult selectedRoute) {

        RouteResult alternativeRoute =
                findAlternativeRoute(
                        graph,
                        sourceId,
                        destinationId,
                        selectedRoute.getPath()
                );

        return new RouteComparison(
                selectedRoute,
                alternativeRoute
        );
    }


    // =========================================
    // FIND ALTERNATIVE ROUTE
    // =========================================

    public RouteResult findAlternativeRoute(
            Graph graph,
            int sourceId,
            int destinationId,
            List<Node> selectedRoute) {

        List<Edge> blockedEdges =
                new ArrayList<>();

        // Find the edges used by the selected route
        for (int i = 0;
             i < selectedRoute.size() - 1;
             i++) {

            Node currentNode =
                    selectedRoute.get(i);

            Node nextNode =
                    selectedRoute.get(i + 1);

            for (Edge edge :
                    graph.getNeighbors(
                            currentNode.getId()
                    )) {

                if (edge.getDestination().getId()
                        == nextNode.getId()) {

                    blockedEdges.add(edge);
                    break;
                }
            }
        }


        // Block the first edge of the
        // selected route to find an
        // alternative route.

        if (!blockedEdges.isEmpty()) {

            Edge blockedEdge =
                    blockedEdges.get(0);

            Graph alternativeGraph =
                    createGraphWithoutEdge(
                            graph,
                            blockedEdge
                    );

            DijkstraAlgorithm dijkstra =
                    new DijkstraAlgorithm();

            return dijkstra.findShortestPath(
                    alternativeGraph,
                    sourceId,
                    destinationId
            );
        }


        // No alternative route
        return new RouteResult(
                new ArrayList<>(),
                Double.POSITIVE_INFINITY
        );
    }


    // =========================================
    // CREATE GRAPH WITHOUT ONE EDGE
    // =========================================

    private Graph createGraphWithoutEdge(
            Graph originalGraph,
            Edge edgeToRemove) {

        Graph newGraph =
                new Graph();


        // Copy all nodes
        for (Node node :
                originalGraph.getAllNodes()) {

            newGraph.addNode(node);
        }


        // Copy all edges except
        // the blocked edge

        for (Node node :
                originalGraph.getAllNodes()) {

            for (Edge edge :
                    originalGraph.getNeighbors(
                            node.getId()
                    )) {

                if (edge != edgeToRemove) {

                    Edge copiedEdge =
                            new Edge(
                                    edge.getSource(),
                                    edge.getDestination(),
                                    edge.getDistance(),
                                    edge.getSpeedKmh()
                            );

                    copiedEdge.setTrafficLevel(
                            edge.getTrafficLevel()
                    );

                    newGraph.addEdge(
                            copiedEdge
                    );
                }
            }
        }

        return newGraph;
    }
}