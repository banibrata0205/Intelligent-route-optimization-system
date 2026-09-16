package osm;

import graph.Graph;
import model.Edge;
import model.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsmGraphConverter {

    private final OsmNodeMapper nodeMapper;

    private final Map<Long, Node> convertedNodes =
            new HashMap<>();

    public OsmGraphConverter(
            OsmNodeMapper nodeMapper) {

        this.nodeMapper =
                nodeMapper;
    }

    public Graph createGraph(
            List<OsmRoad> roads,
            Map<Long, org.openstreetmap.osmosis.core.domain.v0_6.Node> osmNodes) {

        Graph graph =
                new Graph();

        for (OsmRoad road : roads) {

            for (Long osmNodeId :
                    road.getNodeIds()) {

                if (convertedNodes.containsKey(
                        osmNodeId)) {

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

        return graph;
    }

    public int getConvertedNodeCount() {

        return convertedNodes.size();
    }
}