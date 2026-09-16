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

    public Graph buildGraph(
            List<OsmRoad> roads,
            Map<Long, org.openstreetmap.osmosis.core.domain.v0_6.Node> osmNodes) {

        Graph graph =
                new Graph();

        /*
         * First create all graph nodes
         * referenced by the extracted roads.
         */
        createGraphNodes(
                graph,
                roads,
                osmNodes
        );

        /*
         * Then create the actual road edges.
         */
        createGraphEdges(
                graph,
                roads,
                osmNodes
        );

        return graph;
    }

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
                        osmNodes.get(osmNodeId);

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

                graph.addNode(node);
            }
        }
    }

    private void createGraphEdges(
            Graph graph,
            List<OsmRoad> roads,
            Map<Long, org.openstreetmap.osmosis.core.domain.v0_6.Node> osmNodes) {

        for (OsmRoad road : roads) {

            List<Long> nodeIds =
                    road.getNodeIds();

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
                        osmNodes.get(firstOsmId);

                org.openstreetmap.osmosis.core.domain.v0_6.Node secondOsmNode =
                        osmNodes.get(secondOsmId);

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

                double speedKmh =
                        getDefaultSpeed(
                                road.getHighwayType()
                        );

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
                 * Add the reverse edge only
                 * when the OSM road is not one-way.
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

    public int getConvertedNodeCount() {

        return convertedNodes.size();
    }
}