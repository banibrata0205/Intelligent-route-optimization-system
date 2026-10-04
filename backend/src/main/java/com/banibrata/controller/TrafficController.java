package com.banibrata.controller;

import graph.Graph;
import model.Edge;
import model.TrafficLevel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import traffic.TrafficManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {

    private final Graph graph;
    private final TrafficManager trafficManager;

    public TrafficController(
            Graph graph,
            TrafficManager trafficManager) {

        this.graph = graph;
        this.trafficManager = trafficManager;
    }

    // =========================================================
    // GET A SAMPLE ROAD
    // =========================================================

    @GetMapping("/sample")
    public Map<String, Object> getSampleRoad() {

        for (var node : graph.getAllNodes()) {

            List<Edge> edges =
                    graph.getNeighbors(node.getId());

            if (!edges.isEmpty()) {

                Edge edge = edges.get(0);

                return createRoadResponse(edge);
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No road found in graph."
        );
    }

    // =========================================================
    // GET CURRENT TRAFFIC OF A ROAD
    // =========================================================

    @GetMapping("/road")
    public Map<String, Object> getRoadTraffic(
            @RequestParam int sourceId,
            @RequestParam int destinationId) {

        Edge edge =
                findEdge(sourceId, destinationId);

        return createRoadResponse(edge);
    }

    // =========================================================
    // UPDATE ONE DIRECTION
    // =========================================================

    @PutMapping("/road")
    public Map<String, Object> updateRoadTraffic(
            @RequestParam int sourceId,
            @RequestParam int destinationId,
            @RequestParam String trafficLevel) {

        TrafficLevel level =
                parseTrafficLevel(trafficLevel);

        Edge edge =
                findEdge(sourceId, destinationId);

        TrafficLevel oldLevel =
                edge.getTrafficLevel();

        trafficManager.updateRoadTraffic(
                graph,
                sourceId,
                destinationId,
                level
        );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("message",
                "Traffic updated successfully.");

        response.put("sourceId",
                sourceId);

        response.put("destinationId",
                destinationId);

        response.put("oldTraffic",
                oldLevel);

        response.put("newTraffic",
                level);

        response.put("effectiveSpeedKmh",
                edge.getEffectiveSpeedKmh());

        response.put("travelTimeMinutes",
                edge.getTravelTimeMinutes());

        response.put("trafficAdjustedCost",
                edge.getTravelCost());

        return response;
    }

    // =========================================================
    // UPDATE BOTH DIRECTIONS
    // =========================================================

    @PutMapping("/bidirectional")
    public Map<String, Object> updateBidirectionalTraffic(
            @RequestParam int nodeA,
            @RequestParam int nodeB,
            @RequestParam String trafficLevel) {

        TrafficLevel level =
                parseTrafficLevel(trafficLevel);

        Edge edgeAB =
                findEdge(nodeA, nodeB);

        Edge edgeBA =
                findEdge(nodeB, nodeA);

        trafficManager.updateBidirectionalTraffic(
                graph,
                nodeA,
                nodeB,
                level
        );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("message",
                "Bidirectional traffic updated successfully.");

        response.put("nodeA",
                nodeA);

        response.put("nodeB",
                nodeB);

        response.put("trafficLevel",
                level);

        response.put("roadABTravelTimeMinutes",
                edgeAB.getTravelTimeMinutes());

        response.put("roadBATravelTimeMinutes",
                edgeBA.getTravelTimeMinutes());

        response.put("roadABCost",
                edgeAB.getTravelCost());

        response.put("roadBACost",
                edgeBA.getTravelCost());

        return response;
    }

    // =========================================================
    // FIND EDGE
    // =========================================================

    private Edge findEdge(
            int sourceId,
            int destinationId) {

        if (graph.getNode(sourceId) == null) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Source node not found: "
                            + sourceId
            );
        }

        if (graph.getNode(destinationId) == null) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Destination node not found: "
                            + destinationId
            );
        }

        for (Edge edge :
                graph.getNeighbors(sourceId)) {

            if (edge.getDestination().getId()
                    == destinationId) {

                return edge;
            }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Road not found from "
                        + sourceId
                        + " to "
                        + destinationId
        );
    }

    // =========================================================
    // PARSE TRAFFIC LEVEL
    // =========================================================

    private TrafficLevel parseTrafficLevel(
            String trafficLevel) {

        try {

            return TrafficLevel.valueOf(
                    trafficLevel.trim().toUpperCase()
            );

        } catch (IllegalArgumentException ex) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid traffic level. Use NORMAL, LIGHT, MODERATE, HEAVY or SEVERE."
            );
        }
    }

    // =========================================================
    // ROAD RESPONSE
    // =========================================================

    private Map<String, Object> createRoadResponse(
            Edge edge) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("sourceId",
                edge.getSource().getId());

        response.put("sourceName",
                edge.getSource().getName());

        response.put("destinationId",
                edge.getDestination().getId());

        response.put("destinationName",
                edge.getDestination().getName());

        response.put("distanceKm",
                edge.getDistance());

        response.put("speedKmh",
                edge.getSpeedKmh());

        response.put("trafficLevel",
                edge.getTrafficLevel());

        response.put("trafficMultiplier",
                edge.getTrafficLevel().getMultiplier());

        response.put("effectiveSpeedKmh",
                edge.getEffectiveSpeedKmh());

        response.put("travelTimeMinutes",
                edge.getTravelTimeMinutes());

        response.put("trafficAdjustedCost",
                edge.getTravelCost());

        return response;
    }
}
