package algorithm;

import graph.Graph;
import model.Edge;
import model.MultiStopRoute;
import model.Node;
import model.RouteResult;

import java.util.ArrayList;
import java.util.List;

public class MultiStopOptimizer {

    private Graph graph;

    private DijkstraAlgorithm dijkstra;

    private List<Node> bestRoute;

    private double bestTravelTime;

    private double bestDistance;

    public MultiStopOptimizer(Graph graph) {

        this.graph = graph;

        this.dijkstra =
                new DijkstraAlgorithm();

        this.bestRoute =
                new ArrayList<>();

        this.bestTravelTime =
                Double.POSITIVE_INFINITY;

        this.bestDistance =
                Double.POSITIVE_INFINITY;
    }

    public MultiStopRoute findOptimalRoute(
            Node start,
            List<Node> stops) {

        bestRoute =
                new ArrayList<>();

        bestTravelTime =
                Double.POSITIVE_INFINITY;

        bestDistance =
                Double.POSITIVE_INFINITY;

        List<Node> currentRoute =
                new ArrayList<>();

        currentRoute.add(start);

        findPermutations(
                currentRoute,
                stops
        );

        return new MultiStopRoute(
                bestRoute,
                bestDistance,
                bestTravelTime
        );
    }

    private void findPermutations(
            List<Node> currentRoute,
            List<Node> remainingStops) {

        if (remainingStops.isEmpty()) {

            evaluateRoute(
                    currentRoute
            );

            return;
        }

        for (int i = 0;
             i < remainingStops.size();
             i++) {

            Node nextStop =
                    remainingStops.get(i);

            currentRoute.add(
                    nextStop
            );

            List<Node> newRemainingStops =
                    new ArrayList<>(
                            remainingStops
                    );

            newRemainingStops.remove(i);

            findPermutations(
                    currentRoute,
                    newRemainingStops
            );

            currentRoute.remove(
                    currentRoute.size() - 1
            );
        }
    }

    private void evaluateRoute(
            List<Node> route) {

        double totalTravelTime = 0.0;

        double totalDistance = 0.0;

        for (int i = 0;
             i < route.size() - 1;
             i++) {

            Node source =
                    route.get(i);

            Node destination =
                    route.get(i + 1);

            RouteResult result =
                    dijkstra.findShortestPath(
                            graph,
                            source.getId(),
                            destination.getId()
                    );

            if (result.getPath().isEmpty()) {

                return;
            }

            totalTravelTime +=
                    result.getTotalTravelTimeHours();

            double segmentDistance =
                    calculatePathDistance(
                            result.getPath()
                    );

            totalDistance +=
                    segmentDistance;
        }

        if (totalTravelTime <
                bestTravelTime) {

            bestTravelTime =
                    totalTravelTime;

            bestDistance =
                    totalDistance;

            bestRoute =
                    new ArrayList<>(
                            route
                    );
        }
    }

    private double calculatePathDistance(
            List<Node> path) {

        double distance = 0.0;

        for (int i = 0;
             i < path.size() - 1;
             i++) {

            Node source =
                    path.get(i);

            Node destination =
                    path.get(i + 1);

            for (Edge edge :
                    graph.getNeighbors(
                            source.getId()
                    )) {

                if (edge.getDestination()
                        .getId()
                        == destination.getId()) {

                    distance +=
                            edge.getDistance();

                    break;
                }
            }
        }

        return distance;
    }

    public void displayRouteSegments() {

        if (bestRoute.isEmpty()) {

            System.out.println(
                    "No route segments available."
            );

            return;
        }

        System.out.println();
        System.out.println(
                "===== ROUTE SEGMENTS ====="
        );

        for (int i = 0;
             i < bestRoute.size() - 1;
             i++) {

            Node source =
                    bestRoute.get(i);

            Node destination =
                    bestRoute.get(i + 1);

            RouteResult result =
                    dijkstra.findShortestPath(
                            graph,
                            source.getId(),
                            destination.getId()
                    );

            System.out.println();

            System.out.println(
                    source.getName()
                            + " -> "
                            + destination.getName()
            );

            System.out.print(
                    "Actual path: "
            );

            displayPath(
                    result.getPath()
            );

            double distance =
                    calculatePathDistance(
                            result.getPath()
                    );

            System.out.printf(
                    "Distance: %.2f km%n",
                    distance
            );

            System.out.printf(
                    "Travel time: %.2f minutes%n",
                    result.getTotalTravelTimeMinutes()
            );
        }
    }

    private void displayPath(
            List<Node> path) {

        for (int i = 0;
             i < path.size();
             i++) {

            System.out.print(
                    path.get(i).getName()
            );

            if (i < path.size() - 1) {

                System.out.print(
                        " -> "
                );
            }
        }

        System.out.println();
    }

    public double getBestTravelTimeHours() {

        return bestTravelTime;
    }

    public double getBestTravelTimeMinutes() {

        return bestTravelTime * 60;
    }

    public double getBestDistanceKm() {

        return bestDistance;
    }
}