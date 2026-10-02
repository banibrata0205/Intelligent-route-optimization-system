package osm;

import graph.Graph;
import model.Edge;
import model.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsmGraphBuilder {

    private final OsmNodeMapper nodeMapper;

    private final Map<Long, Node> convertedNodes =
            new HashMap<>();

    public OsmGraphBuilder(
            OsmNodeMapper nodeMapper) {

        this.nodeMapper =
                nodeMapper;
    }

    // ---------------------------------------------------------
    // Build complete graph
    // ---------------------------------------------------------

    public Graph buildGraph(
            List<OsmRoad> roads,
            Map<Long, org.openstreetmap.osmosis.core.domain.v0_6.Node> osmNodes) {

        Graph graph =
                new Graph();

        createGraphNodes(
                graph,
                roads,
                osmNodes
        );

        createGraphEdges(
                graph,
                roads,
                osmNodes
        );

        return graph;
    }

    // ---------------------------------------------------------
    // Create graph nodes
    // ---------------------------------------------------------

    private void createGraphNodes(
            Graph graph,
            List<OsmRoad> roads,
            Map<Long, org.openstreetmap.osmosis.core.domain.v0_6.Node> osmNodes) {

        for (OsmRoad road : roads) {

            for (Long osmNodeId :
                    road.getNodeIds()) {

                if (convertedNodes.containsKey(
                        osmNodeId
                )) {

                    continue;
                }

                org.openstreetmap.osmosis.core.domain.v0_6.Node osmNode =
                        osmNodes.get(
                                osmNodeId
                        );

                if (osmNode == null) {

                    continue;
                }

                int internalId =
                        nodeMapper
                                .getOrCreateInternalId(
                                        osmNodeId
                                );

                Node node =
                        new Node(
                                internalId,
                                "OSM Node "
                                        + osmNodeId,
                                osmNode.getLatitude(),
                                osmNode.getLongitude()
                        );

                convertedNodes.put(
                        osmNodeId,
                        node
                );

                graph.addNode(
                        node
                );
            }
        }
    }

    // ---------------------------------------------------------
    // Create graph edges
    // ---------------------------------------------------------

    private void createGraphEdges(
            Graph graph,
            List<OsmRoad> roads,
            Map<Long, org.openstreetmap.osmosis.core.domain.v0_6.Node> osmNodes) {

        for (OsmRoad road : roads) {

            List<Long> nodeIds =
                    road.getNodeIds();

            /*
             * Get speed from OSM maxspeed
             * when available. Otherwise use
             * the default speed for the
             * highway type.
             */
            double speedKmh =
                    getSpeedKmh(
                            road
                    );

            for (int i = 0;
                 i < nodeIds.size() - 1;
                 i++) {

                Long firstOsmId =
                        nodeIds.get(i);

                Long secondOsmId =
                        nodeIds.get(i + 1);

                Node firstNode =
                        convertedNodes.get(
                                firstOsmId
                        );

                Node secondNode =
                        convertedNodes.get(
                                secondOsmId
                        );

                if (firstNode == null
                        || secondNode == null) {

                    continue;
                }

                org.openstreetmap.osmosis.core.domain.v0_6.Node firstOsmNode =
                        osmNodes.get(
                                firstOsmId
                        );

                org.openstreetmap.osmosis.core.domain.v0_6.Node secondOsmNode =
                        osmNodes.get(
                                secondOsmId
                        );

                if (firstOsmNode == null
                        || secondOsmNode == null) {

                    continue;
                }

                double distanceKm =
                        OsmDistanceCalculator
                                .calculateDistance(
                                        firstOsmNode,
                                        secondOsmNode
                                );

                // Forward direction
                Edge forwardEdge =
                        new Edge(
                                firstNode,
                                secondNode,
                                distanceKm,
                                speedKmh
                        );

                graph.addEdge(
                        forwardEdge
                );

                /*
                 * OSM one-way roads only get
                 * the forward edge.
                 */
                if (!road.isOneWay()) {

                    Edge reverseEdge =
                            new Edge(
                                    secondNode,
                                    firstNode,
                                    distanceKm,
                                    speedKmh
                            );

                    graph.addEdge(
                            reverseEdge
                    );
                }
            }
        }
    }

    // ---------------------------------------------------------
    // Select road speed
    // ---------------------------------------------------------

    private double getSpeedKmh(
            OsmRoad road) {

        /*
         * Prefer the real OSM maxspeed
         * when it is available and valid.
         */
        if (road.hasMaxSpeed()
                && road.getMaxSpeedKmh() != null
                && road.getMaxSpeedKmh() > 0) {

            return road.getMaxSpeedKmh();
        }

        /*
         * Otherwise fall back to a
         * highway-type based estimate.
         */
        return getDefaultSpeed(
                road.getHighwayType()
        );
    }

    // ---------------------------------------------------------
    // Default speed by road type
    // ---------------------------------------------------------

    private double getDefaultSpeed(
            String highwayType) {

        if (highwayType == null) {

            return 30.0;
        }

        switch (highwayType) {

            case "motorway":
                return 80.0;

            case "trunk":
                return 70.0;

            case "primary":
                return 60.0;

            case "secondary":
                return 50.0;

            case "tertiary":
                return 40.0;

            case "unclassified":
                return 35.0;

            case "residential":
                return 30.0;

            case "living_street":
                return 20.0;

            case "service":
                return 20.0;

            default:
                return 30.0;
        }
    }

    // ---------------------------------------------------------
    // Number of converted graph nodes
    // ---------------------------------------------------------

    public int getConvertedNodeCount() {

        return convertedNodes.size();
    }
}