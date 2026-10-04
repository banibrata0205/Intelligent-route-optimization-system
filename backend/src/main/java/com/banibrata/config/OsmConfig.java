package com.banibrata.config;

import graph.Graph;
import osm.OsmGraphBuilder;
import osm.OsmNodeMapper;
import osm.OsmPbfReader;
import osm.OsmRoad;
import osm.OsmRouteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OsmConfig {

    private static final String OSM_FILE =
            "data/osm/eastern-zone-latest.osm.pbf";

    @Bean
    public Graph osmGraph() {

        System.out.println();
        System.out.println("===== SPRING OSM INITIALIZATION =====");
        System.out.println("Loading OSM road network...");

        OsmPbfReader reader =
                new OsmPbfReader();

        reader.read(OSM_FILE);

        List<OsmRoad> roads =
                reader.getOsmRoads();

        var osmNodes =
                reader.getNodeStore();

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

        System.out.println(
                "OSM roads loaded: " + roads.size()
        );

        System.out.println(
                "Graph nodes: " + graph.getAllNodes().size()
        );

        System.out.println(
                "Graph maximum speed: "
                        + graph.getMaximumSpeedKmh()
                        + " km/h"
        );

        System.out.println(
                "===== OSM INITIALIZATION COMPLETE ====="
        );

        return graph;
    }

    @Bean
    public OsmRouteService osmRouteService(
            Graph osmGraph) {

        return new OsmRouteService(
                osmGraph
        );
    }
}
