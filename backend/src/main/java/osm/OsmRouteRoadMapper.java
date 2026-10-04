package osm;

import model.Node;
import model.RouteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsmRouteRoadMapper {

    /*
     * Directed segment:
     *
     * OSM Node A -> OSM Node B
     *
     * maps to an OsmRoad.
     */
    private final Map<String, OsmRoad> roadSegmentMap =
            new HashMap<>();

    public OsmRouteRoadMapper(
            List<OsmRoad> roads) {

        buildSegmentMap(roads);
    }

    // ---------------------------------------------------------
    // Build road segment lookup
    // ---------------------------------------------------------

    private void buildSegmentMap(
            List<OsmRoad> roads) {

        for (OsmRoad road : roads) {

            List<Long> nodeIds =
                    road.getNodeIds();

            for (int i = 0;
                 i < nodeIds.size() - 1;
                 i++) {

                long firstNodeId =
                        nodeIds.get(i);

                long secondNodeId =
                        nodeIds.get(i + 1);

                // Forward direction
                roadSegmentMap.putIfAbsent(
                        createKey(
                                firstNodeId,
                                secondNodeId
                        ),
                        road
                );

                /*
                 * Reverse direction only for
                 * two-way roads.
                 */
                if (!road.isOneWay()) {

                    roadSegmentMap.putIfAbsent(
                            createKey(
                                    secondNodeId,
                                    firstNodeId
                            ),
                            road
                    );
                }
            }
        }
    }

    // ---------------------------------------------------------
    // Find OSM road using OSM node IDs
    // ---------------------------------------------------------

    public OsmRoad findRoad(
            long fromNodeId,
            long toNodeId) {

        return roadSegmentMap.get(
                createKey(
                        fromNodeId,
                        toNodeId
                )
        );
    }

    // ---------------------------------------------------------
    // Find OSM road using internal graph node IDs
    // ---------------------------------------------------------

    public OsmRoad findRoad(
            Node fromNode,
            Node toNode,
            OsmNodeMapper nodeMapper) {

        Long fromOsmId =
                nodeMapper.getOsmNodeId(
                        fromNode.getId()
                );

        Long toOsmId =
                nodeMapper.getOsmNodeId(
                        toNode.getId()
                );

        if (fromOsmId == null
                || toOsmId == null) {

            return null;
        }

        return findRoad(
                fromOsmId,
                toOsmId
        );
    }

    // ---------------------------------------------------------
    // Get roads for a complete route
    // ---------------------------------------------------------

    public List<OsmRoad> findRoadsForRoute(
            RouteResult route,
            OsmNodeMapper nodeMapper) {

        List<OsmRoad> routeRoads =
                new ArrayList<>();

        List<Node> path =
                route.getPath();

        if (path == null
                || path.size() < 2) {

            return routeRoads;
        }

        for (int i = 0;
             i < path.size() - 1;
             i++) {

            Node fromNode =
                    path.get(i);

            Node toNode =
                    path.get(i + 1);

            OsmRoad road =
                    findRoad(
                            fromNode,
                            toNode,
                            nodeMapper
                    );

            if (road != null) {

                routeRoads.add(
                        road
                );
            }
        }

        return routeRoads;
    }

    // ---------------------------------------------------------
    // Create lookup key
    // ---------------------------------------------------------

    private String createKey(
            long fromNodeId,
            long toNodeId) {

        return fromNodeId
                + "->"
                + toNodeId;
    }

    // ---------------------------------------------------------
    // Number of indexed segments
    // ---------------------------------------------------------

    public int getIndexedSegmentCount() {

        return roadSegmentMap.size();
    }
}