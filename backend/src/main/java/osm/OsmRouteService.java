package osm;

import algorithm.AStarAlgorithm;
import algorithm.DijkstraAlgorithm;
import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteResult;

import java.util.Collections;
import java.util.List;

public class OsmRouteService {

    private final Graph graph;

    private final OsmNearestNodeFinder nearestNodeFinder;

    private final DijkstraAlgorithm dijkstra;

    private final AStarAlgorithm aStar;

    public OsmRouteService(Graph graph) {

        this.graph = graph;

        this.nearestNodeFinder = new OsmNearestNodeFinder();

        this.dijkstra = new DijkstraAlgorithm();

        this.aStar = new AStarAlgorithm();
    }

    public RouteResult findRouteWithDijkstra(
            double startLatitude,
            double startLongitude,
            double endLatitude,
            double endLongitude) {

        Node startNode = findNearestNode(
                startLatitude,
                startLongitude
        );

        Node endNode = findNearestNode(
                endLatitude,
                endLongitude
        );

        if (startNode == null || endNode == null) {

            return new RouteResult(
                    Collections.emptyList(),
                    Double.POSITIVE_INFINITY
            );
        }

        return dijkstra.findShortestPath(
                graph,
                startNode.getId(),
                endNode.getId()
        );
    }

    public RouteResult findRouteWithAStar(
            double startLatitude,
            double startLongitude,
            double endLatitude,
            double endLongitude) {

        Node startNode = findNearestNode(
                startLatitude,
                startLongitude
        );

        Node endNode = findNearestNode(
                endLatitude,
                endLongitude
        );

        if (startNode == null || endNode == null) {

            return new RouteResult(
                    Collections.emptyList(),
                    Double.POSITIVE_INFINITY
            );
        }

        return aStar.findShortestPath(
                graph,
                startNode.getId(),
                endNode.getId()
        );
    }

    public Node findNearestNode(
            double latitude,
            double longitude) {

        return nearestNodeFinder.findNearestNode(
                graph,
                latitude,
                longitude
        );
    }

    // -----------------------------------------
    // Calculate total route distance
    // -----------------------------------------

    public double calculateRouteDistanceKm(
            RouteResult route) {

        if (route == null ||
                route.getPath() == null ||
                route.getPath().size() < 2) {

            return 0.0;
        }

        List<Node> path = route.getPath();

        double totalDistanceKm = 0.0;

        for (int i = 0; i < path.size() - 1; i++) {

            Node fromNode = path.get(i);

            Node toNode = path.get(i + 1);

            List<Edge> neighbors =
                    graph.getNeighbors(
                            fromNode.getId()
                    );

            boolean edgeFound = false;

            for (Edge edge : neighbors) {

                if (edge.getDestination().getId()
                        == toNode.getId()) {

                    totalDistanceKm +=
                            edge.getDistance();

                    edgeFound = true;

                    break;
                }
            }

            if (!edgeFound) {

                System.out.println(
                        "Warning: route edge not found between "
                                + fromNode.getId()
                                + " and "
                                + toNode.getId()
                );
            }
        }

        return totalDistanceKm;
    }
}