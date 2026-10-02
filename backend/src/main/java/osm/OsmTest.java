package osm;

import graph.Graph;
import model.Node;
import model.RouteResult;

import java.util.List;

public class OsmTest {

    public static void main(String[] args) {

        String osmFile =
                "data/osm/eastern-zone-latest.osm.pbf";

        // -------------------------------------------------
        // Read OSM data
        // -------------------------------------------------

        OsmPbfReader reader =
                new OsmPbfReader();

        reader.read(osmFile);

        List<OsmRoad> roads =
                reader.getOsmRoads();

        var osmNodes =
                reader.getNodeStore();

        // -------------------------------------------------
        // Build graph
        // -------------------------------------------------

        OsmNodeMapper nodeMapper =
                new OsmNodeMapper();

        OsmGraphBuilder graphBuilder =
                new OsmGraphBuilder(
                        nodeMapper
                );

        Graph graph =
                graphBuilder.buildGraph(
                        roads,
                        osmNodes
                );

        // -------------------------------------------------
        // Route service
        // -------------------------------------------------

        OsmRouteService routeService =
                new OsmRouteService(
                        graph
                );

        // -------------------------------------------------
        // Start coordinates
        // -------------------------------------------------

        double startLatitude =
                22.5726;

        double startLongitude =
                88.3639;

        // -------------------------------------------------
        // Destination coordinates
        // -------------------------------------------------

        double endLatitude =
                22.6500;

        double endLongitude =
                88.4460;

        // -------------------------------------------------
        // Find nearest nodes
        // -------------------------------------------------

        Node startNode =
                routeService.findNearestNode(
                        startLatitude,
                        startLongitude
                );

        Node endNode =
                routeService.findNearestNode(
                        endLatitude,
                        endLongitude
                );

        System.out.println();

        System.out.println(
                "===== GEOGRAPHIC ROUTING TEST ====="
        );

        System.out.println();

        System.out.println(
                "Start coordinates: "
                        + startLatitude
                        + ", "
                        + startLongitude
        );

        System.out.println(
                "Nearest start node:"
        );

        System.out.println(
                startNode
        );

        System.out.println();

        System.out.println(
                "Destination coordinates: "
                        + endLatitude
                        + ", "
                        + endLongitude
        );

        System.out.println(
                "Nearest destination node:"
        );

        System.out.println(
                endNode
        );

        if (startNode == null
                || endNode == null) {

            System.out.println();

            System.out.println(
                    "Unable to find graph nodes."
            );

            return;
        }

        // -------------------------------------------------
        // Dijkstra route
        // -------------------------------------------------

        RouteResult dijkstraRoute =
                routeService.findRouteWithDijkstra(
                        startLatitude,
                        startLongitude,
                        endLatitude,
                        endLongitude
                );

        // -------------------------------------------------
        // A* route
        // -------------------------------------------------

        RouteResult aStarRoute =
                routeService.findRouteWithAStar(
                        startLatitude,
                        startLongitude,
                        endLatitude,
                        endLongitude
                );

        // -------------------------------------------------
        // Calculate distances
        // -------------------------------------------------

        double dijkstraDistanceKm =
                OsmRouteDistanceCalculator
                        .calculateDistanceKm(
                                dijkstraRoute.getPath()
                        );

        double aStarDistanceKm =
                OsmRouteDistanceCalculator
                        .calculateDistanceKm(
                                aStarRoute.getPath()
                        );

        // -------------------------------------------------
        // Display Dijkstra
        // -------------------------------------------------

        System.out.println();

        System.out.println(
                "===== DIJKSTRA ROUTE ====="
        );

        displayRoute(
                dijkstraRoute,
                dijkstraDistanceKm
        );

        // -------------------------------------------------
        // Display A*
        // -------------------------------------------------

        System.out.println();

        System.out.println(
                "===== A* ROUTE ====="
        );

        displayRoute(
                aStarRoute,
                aStarDistanceKm
        );

        // -------------------------------------------------
        // Compare
        // -------------------------------------------------

        if (!dijkstraRoute
                .getPath()
                .isEmpty()
                &&
                !aStarRoute
                        .getPath()
                        .isEmpty()) {

            double dijkstraTime =
                    dijkstraRoute
                            .getTotalTravelTimeMinutes();

            double aStarTime =
                    aStarRoute
                            .getTotalTravelTimeMinutes();

            System.out.println();

            System.out.println(
                    "===== ROUTE COMPARISON ====="
            );

            System.out.printf(
                    "Dijkstra distance: %.2f km%n",
                    dijkstraDistanceKm
            );

            System.out.printf(
                    "A* distance: %.2f km%n",
                    aStarDistanceKm
            );

            System.out.printf(
                    "Dijkstra time: %.2f minutes%n",
                    dijkstraTime
            );

            System.out.printf(
                    "A* time: %.2f minutes%n",
                    aStarTime
            );

            System.out.printf(
                    "Time difference: %.6f minutes%n",
                    Math.abs(
                            dijkstraTime
                                    - aStarTime
                    )
            );
        }
    }

    private static void displayRoute(
            RouteResult route,
            double distanceKm) {

        if (route.getPath().isEmpty()) {

            System.out.println(
                    "No route found."
            );

            return;
        }

        System.out.println(
                "Number of nodes: "
                        + route
                                .getPath()
                                .size()
        );

        System.out.printf(
                "Distance: %.2f km%n",
                distanceKm
        );

        System.out.printf(
                "Travel time: %.2f minutes%n",
                route
                        .getTotalTravelTimeMinutes()
        );

        System.out.print(
                "Path: "
        );

        int displayLimit =
                Math.min(
                        20,
                        route
                                .getPath()
                                .size()
                );

        for (int i = 0;
             i < displayLimit;
             i++) {

            System.out.print(
                    route
                            .getPath()
                            .get(i)
                            .getName()
            );

            if (i <
                    displayLimit - 1) {

                System.out.print(
                        " -> "
                );
            }
        }

        if (route.getPath().size()
                > displayLimit) {

            System.out.print(
                    " -> ..."
            );
        }

        System.out.println();
    }
}