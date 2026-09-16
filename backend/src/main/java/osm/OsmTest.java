package osm;

import algorithm.AStarAlgorithm;
import algorithm.DijkstraAlgorithm;
import graph.Graph;
import model.Edge;
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

        // -------------------------------------------------
        // Get OSM data
        // -------------------------------------------------

        List<OsmRoad> roads =
                reader.getOsmRoads();

        var osmNodes =
                reader.getNodeStore();

        // -------------------------------------------------
        // Create node ID mapper
        // -------------------------------------------------

        OsmNodeMapper nodeMapper =
                new OsmNodeMapper();

        // -------------------------------------------------
        // Build graph
        // -------------------------------------------------

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
        // Count edges
        // -------------------------------------------------

        long edgeCount = 0;

        Node sourceNode = null;
        Node destinationNode = null;

        Edge firstEdge = null;

        for (Node node :
                graph.getAllNodes()) {

            List<Edge> neighbors =
                    graph.getNeighbors(
                            node.getId()
                    );

            edgeCount +=
                    neighbors.size();

            if (sourceNode == null
                    && !neighbors.isEmpty()) {

                sourceNode = node;

                firstEdge =
                        neighbors.get(0);

                destinationNode =
                        firstEdge
                                .getDestination();
            }
        }

        // -------------------------------------------------
        // Display graph statistics
        // -------------------------------------------------

        System.out.println();

        System.out.println(
                "===== OSM GRAPH VALIDATION ====="
        );

        System.out.println(
                "OSM roads: "
                        + roads.size()
        );

        System.out.println(
                "OSM nodes stored: "
                        + osmNodes.size()
        );

        System.out.println(
                "Graph nodes: "
                        + graph
                                .getAllNodes()
                                .size()
        );

        System.out.println(
                "Graph edges: "
                        + edgeCount
        );

        // -------------------------------------------------
        // Check whether a connected road was found
        // -------------------------------------------------

        if (sourceNode == null
                || destinationNode == null) {

            System.out.println();

            System.out.println(
                    "No connected road was found."
            );

            return;
        }

        // -------------------------------------------------
        // Display test road
        // -------------------------------------------------

        System.out.println();

        System.out.println(
                "===== REAL OSM ROAD TEST ====="
        );

        System.out.println(
                "Source:"
        );

        System.out.println(
                sourceNode
        );

        System.out.println();

        System.out.println(
                "Destination:"
        );

        System.out.println(
                destinationNode
        );

        System.out.println();

        System.out.println(
                "Road:"
        );

        System.out.println(
                firstEdge
        );

        // -------------------------------------------------
        // Run Dijkstra
        // -------------------------------------------------

        DijkstraAlgorithm dijkstra =
                new DijkstraAlgorithm();

        RouteResult dijkstraRoute =
                dijkstra.findShortestPath(
                        graph,
                        sourceNode.getId(),
                        destinationNode.getId()
                );

        // -------------------------------------------------
        // Run A*
        // -------------------------------------------------

        AStarAlgorithm aStar =
                new AStarAlgorithm();

        RouteResult aStarRoute =
                aStar.findShortestPath(
                        graph,
                        sourceNode.getId(),
                        destinationNode.getId()
                );

        // -------------------------------------------------
        // Display Dijkstra result
        // -------------------------------------------------

        System.out.println();

        System.out.println(
                "===== DIJKSTRA ====="
        );

        displayRoute(
                dijkstraRoute
        );

        // -------------------------------------------------
        // Display A* result
        // -------------------------------------------------

        System.out.println();

        System.out.println(
                "===== A* ====="
        );

        displayRoute(
                aStarRoute
        );

        // -------------------------------------------------
        // Compare results
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
                    "===== ALGORITHM COMPARISON ====="
            );

            System.out.printf(
                    "Dijkstra time: %.4f minutes%n",
                    dijkstraTime
            );

            System.out.printf(
                    "A* time: %.4f minutes%n",
                    aStarTime
            );

            System.out.printf(
                    "Difference: %.6f minutes%n",
                    Math.abs(
                            dijkstraTime
                                    - aStarTime
                    )
            );
        }
    }

    private static void displayRoute(
            RouteResult route) {

        if (route.getPath().isEmpty()) {

            System.out.println(
                    "No route found."
            );

            return;
        }

        System.out.print(
                "Path: "
        );

        for (int i = 0;
             i < route.getPath().size();
             i++) {

            System.out.print(
                    route.getPath()
                            .get(i)
                            .getName()
            );

            if (i <
                    route.getPath().size() - 1) {

                System.out.print(
                        " -> "
                );
            }
        }

        System.out.println();

        System.out.printf(
                "Travel time: %.4f minutes%n",
                route.getTotalTravelTimeMinutes()
        );
    }
}