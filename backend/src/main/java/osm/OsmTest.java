package osm;

import algorithm.DijkstraAlgorithm;
import graph.Graph;
import model.Node;
import model.RouteResult;

import java.util.List;

public class OsmTest {

    public static void main(String[] args) {

        // -------------------------------------------------
        // OSM file
        // -------------------------------------------------

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
        // Geographic coordinates
        // -------------------------------------------------

        double startLatitude =
                22.5726;

        double startLongitude =
                88.3639;

        double endLatitude =
                22.6500;

        double endLongitude =
                88.4460;

        // -------------------------------------------------
        // Find nearest graph nodes
        // -------------------------------------------------

        OsmNearestNodeFinder finder =
                new OsmNearestNodeFinder();

        Node startNode =
                finder.findNearestNode(
                        graph,
                        startLatitude,
                        startLongitude
                );

        Node endNode =
                finder.findNearestNode(
                        graph,
                        endLatitude,
                        endLongitude
                );

        System.out.println();

        System.out.println(
                "===== ROUTE SUMMARY TEST ====="
        );

        System.out.println();

        System.out.println(
                "Start node:"
        );

        System.out.println(
                startNode
        );

        System.out.println();

        System.out.println(
                "Destination node:"
        );

        System.out.println(
                endNode
        );

        if (startNode == null
                || endNode == null) {

            System.out.println();

            System.out.println(
                    "Start or destination node not found."
            );

            return;
        }

        // -------------------------------------------------
        // Calculate Dijkstra route
        // -------------------------------------------------

        DijkstraAlgorithm dijkstra =
                new DijkstraAlgorithm();

        RouteResult route =
                dijkstra.findShortestPath(
                        graph,
                        startNode.getId(),
                        endNode.getId()
                );

        if (route.getPath().isEmpty()) {

            System.out.println();

            System.out.println(
                    "No route found."
            );

            return;
        }

        // -------------------------------------------------
        // Map route to OSM roads
        // -------------------------------------------------

        OsmRouteRoadMapper roadMapper =
                new OsmRouteRoadMapper(
                        roads
                );

        List<OsmRoad> routeRoads =
                roadMapper.findRoadsForRoute(
                        route,
                        nodeMapper
                );

        // -------------------------------------------------
        // Create route summary
        // -------------------------------------------------

        OsmRouteSummary summary =
                new OsmRouteSummary(
                        route,
                        routeRoads
                );

        // -------------------------------------------------
        // Display summary
        // -------------------------------------------------

        summary.display();
    }
}