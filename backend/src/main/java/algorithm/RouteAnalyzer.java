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
    // FIND BEST ALTERNATIVE ROUTE
    // =========================================

    public RouteResult findAlternativeRoute(
            Graph graph,
            int sourceId,
            int destinationId,
            List<Node> selectedRoute) {

        RouteResult bestAlternative =
                new RouteResult(
                        new ArrayList<>(),
                        Double.POSITIVE_INFINITY
                );


        // =========================================
        // TRY BLOCKING EACH EDGE
        // =========================================

        for (int i = 0;
             i < selectedRoute.size() - 1;
             i++) {

            Node currentNode =
                    selectedRoute.get(i);

            Node nextNode =
                    selectedRoute.get(i + 1);


            // =========================================
            // FIND EDGE BETWEEN TWO NODES
            // =========================================

            for (Edge edge :
                    graph.getNeighbors(
                            currentNode.getId()
                    )) {

                if (edge.getDestination().getId()
                        == nextNode.getId()) {


                    // =========================================
                    // CREATE ALTERNATIVE GRAPH
                    // =========================================

                    Graph alternativeGraph =
                            createGraphWithoutEdge(
                                    graph,
                                    edge
                            );


                    // =========================================
                    // FIND CANDIDATE ROUTE
                    // =========================================

                    DijkstraAlgorithm dijkstra =
                            new DijkstraAlgorithm();

                    RouteResult candidateRoute =
                            dijkstra.findShortestPath(
                                    alternativeGraph,
                                    sourceId,
                                    destinationId
                            );


                    // =========================================
                    // CHECK THAT ROUTE IS DIFFERENT
                    // =========================================

                    boolean differentRoute =
                            !candidateRoute
                                    .getPath()
                                    .equals(selectedRoute);


                    // =========================================
                    // CHECK IF CANDIDATE IS BETTER
                    // =========================================

                    if (differentRoute
                            &&
                            candidateRoute
                                    .getTotalTravelTimeHours()
                                    <
                                    bestAlternative
                                            .getTotalTravelTimeHours()) {

                        bestAlternative =
                                candidateRoute;
                    }

                    break;
                }
            }
        }


        return bestAlternative;
    }


    // =========================================
    // CREATE GRAPH WITHOUT ONE EDGE
    // =========================================

    private Graph createGraphWithoutEdge(
            Graph originalGraph,
            Edge edgeToRemove) {

        Graph newGraph =
                new Graph();


        // =========================================
        // COPY ALL NODES
        // =========================================

        for (Node node :
                originalGraph.getAllNodes()) {

            newGraph.addNode(node);
        }


        // =========================================
        // COPY ALL EDGES EXCEPT BLOCKED EDGE
        // =========================================

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


                    // Preserve traffic condition

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