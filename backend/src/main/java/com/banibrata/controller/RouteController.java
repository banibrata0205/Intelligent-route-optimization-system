package com.banibrata.controller;

import com.banibrata.dto.RouteComparisonResponse;
import com.banibrata.dto.RoutePoint;
import com.banibrata.dto.RouteResponse;
import com.banibrata.entity.RouteHistory;
import com.banibrata.repository.RouteHistoryRepository;

import model.Node;
import model.RouteComparison;
import model.RouteResult;

import osm.OsmRouteService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RouteController {

    private final OsmRouteService osmRouteService;
    private final RouteHistoryRepository routeHistoryRepository;

    public RouteController(
            OsmRouteService osmRouteService,
            RouteHistoryRepository routeHistoryRepository) {

        this.osmRouteService = osmRouteService;
        this.routeHistoryRepository = routeHistoryRepository;
    }

    // =========================================================
    // HEALTH CHECK
    // =========================================================

    @GetMapping("/api/health")
    public String healthCheck() {

        return "Intelligent Route Optimization System API is running!";
    }

    // =========================================================
    // FIND ROUTE
    // =========================================================

    @GetMapping("/api/route")
    public RouteResponse findRoute(
            @RequestParam double startLatitude,
            @RequestParam double startLongitude,
            @RequestParam double endLatitude,
            @RequestParam double endLongitude,
            @RequestParam(defaultValue = "dijkstra") String algorithm) {

        // Validate coordinates
        validateCoordinates(
                startLatitude,
                startLongitude,
                endLatitude,
                endLongitude
        );

        // Validate algorithm
        validateAlgorithm(algorithm);

        RouteResult route;
        String selectedAlgorithm;

        // =====================================================
        // SELECT ALGORITHM
        // =====================================================

        if (algorithm.equalsIgnoreCase("astar")) {

            selectedAlgorithm = "A*";

            route = osmRouteService.findRouteWithAStar(
                    startLatitude,
                    startLongitude,
                    endLatitude,
                    endLongitude
            );

        } else {

            selectedAlgorithm = "Dijkstra";

            route = osmRouteService.findRouteWithDijkstra(
                    startLatitude,
                    startLongitude,
                    endLatitude,
                    endLongitude
            );
        }

        // =====================================================
        // VALIDATE RESULT
        // =====================================================

        validateRouteResult(route);

        // =====================================================
        // CREATE RESPONSE
        // =====================================================

        RouteResponse response = createRouteResponse(
                route,
                selectedAlgorithm
        );

        // =====================================================
        // SAVE ROUTE HISTORY
        // =====================================================

        saveRouteHistory(
                startLatitude,
                startLongitude,
                endLatitude,
                endLongitude,
                selectedAlgorithm,
                response
        );

        return response;
    }

    // =========================================================
    // FIND ROUTE FOR TRAFFIC SIMULATION
    // Does NOT save route history
    // =========================================================

    @GetMapping("/api/route/simulation")
    public RouteResponse findSimulationRoute(
            @RequestParam double startLatitude,
            @RequestParam double startLongitude,
            @RequestParam double endLatitude,
            @RequestParam double endLongitude,
            @RequestParam(defaultValue = "dijkstra") String algorithm) {

        validateCoordinates(
                startLatitude,
                startLongitude,
                endLatitude,
                endLongitude
        );

        validateAlgorithm(algorithm);

        RouteResult route;
        String selectedAlgorithm;

        if (algorithm.equalsIgnoreCase("astar")) {

            selectedAlgorithm = "A*";

            route = osmRouteService.findRouteWithAStar(
                    startLatitude,
                    startLongitude,
                    endLatitude,
                    endLongitude
            );

        } else {

            selectedAlgorithm = "Dijkstra";

            route = osmRouteService.findRouteWithDijkstra(
                    startLatitude,
                    startLongitude,
                    endLatitude,
                    endLongitude
            );
        }

        validateRouteResult(route);

        return createRouteResponse(
                route,
                selectedAlgorithm
        );
    }
    // =========================================================
    // COMPARE DIJKSTRA VS A*
    // =========================================================

    @GetMapping("/api/route/compare")
    public RouteComparisonResponse compareRoutes(
            @RequestParam double startLatitude,
            @RequestParam double startLongitude,
            @RequestParam double endLatitude,
            @RequestParam double endLongitude) {

        // Validate coordinates
        validateCoordinates(
                startLatitude,
                startLongitude,
                endLatitude,
                endLongitude
        );

        // =====================================================
        // DIJKSTRA
        // =====================================================

        RouteResult dijkstraRoute =
                osmRouteService.findRouteWithDijkstra(
                        startLatitude,
                        startLongitude,
                        endLatitude,
                        endLongitude
                );

        // =====================================================
        // A*
        // =====================================================

        RouteResult aStarRoute =
                osmRouteService.findRouteWithAStar(
                        startLatitude,
                        startLongitude,
                        endLatitude,
                        endLongitude
                );

        // =====================================================
        // VALIDATE ROUTES
        // =====================================================

        validateRouteResult(dijkstraRoute);
        validateRouteResult(aStarRoute);

        // =====================================================
        // DISTANCE
        // =====================================================

        double dijkstraDistanceKm =
                osmRouteService.calculateRouteDistanceKm(
                        dijkstraRoute
                );

        double aStarDistanceKm =
                osmRouteService.calculateRouteDistanceKm(
                        aStarRoute
                );

        // =====================================================
        // TRAVEL TIME
        // =====================================================

        double dijkstraTimeMinutes =
                dijkstraRoute.getTotalTravelTimeMinutes();

        double aStarTimeMinutes =
                aStarRoute.getTotalTravelTimeMinutes();

        // =====================================================
        // PATH NODES
        // =====================================================

        int dijkstraPathNodes =
                dijkstraRoute.getPath().size();

        int aStarPathNodes =
                aStarRoute.getPath().size();

        // =====================================================
        // ROUTE COMPARISON
        // =====================================================

        RouteComparison comparison =
                new RouteComparison(
                        dijkstraRoute,
                        aStarRoute
                );

        double timeDifferenceMinutes =
                Math.abs(
                        comparison.getTimeSavedMinutes()
                );

        // =====================================================
        // RETURN RESPONSE
        // =====================================================

        return new RouteComparisonResponse(
                dijkstraDistanceKm,
                dijkstraTimeMinutes,
                dijkstraPathNodes,
                aStarDistanceKm,
                aStarTimeMinutes,
                aStarPathNodes,
                timeDifferenceMinutes
        );
    }

    // =========================================================
    // GET RECENT ROUTE HISTORY
    // =========================================================

    @GetMapping("/api/history")
    public List<RouteHistory> getRouteHistory() {

        return routeHistoryRepository
                .findTop20ByOrderByCreatedAtDesc();
    }

    // =========================================================
    // GET ROUTE HISTORY BY ID
    // =========================================================

    @GetMapping("/api/history/{id}")
    public RouteHistory getRouteHistoryById(
            @PathVariable Long id) {

        return routeHistoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Route history not found for id: " + id
                        )
                );
    }

    // =========================================================
    // CREATE ROUTE RESPONSE
    // =========================================================

    private RouteResponse createRouteResponse(
            RouteResult route,
            String algorithm) {

        // Calculate actual route distance
        double totalDistanceKm =
                osmRouteService.calculateRouteDistanceKm(
                        route
                );

        // Get travel time
        double totalTravelTimeHours =
                route.getTotalTravelTimeHours();

        double totalTravelTimeMinutes =
                route.getTotalTravelTimeMinutes();

        // Number of nodes
        int pathNodes =
                route.getPath().size();

        // =====================================================
        // CREATE ROUTE POINT LIST
        // =====================================================

        List<RoutePoint> routePoints =
                new ArrayList<>();

        for (Node node : route.getPath()) {

            routePoints.add(
                    new RoutePoint(
                            node.getId(),
                            node.getLatitude(),
                            node.getLongitude()
                    )
            );
        }

        // =====================================================
        // RETURN RESPONSE
        // =====================================================

        return new RouteResponse(
                algorithm,
                totalDistanceKm,
                totalTravelTimeHours,
                totalTravelTimeMinutes,
                pathNodes,
                routePoints
        );
    }

    // =========================================================
    // SAVE ROUTE HISTORY
    // =========================================================

    private void saveRouteHistory(
            double startLatitude,
            double startLongitude,
            double endLatitude,
            double endLongitude,
            String algorithm,
            RouteResponse response) {

        RouteHistory history =
                new RouteHistory(
                        startLatitude,
                        startLongitude,
                        endLatitude,
                        endLongitude,
                        algorithm,
                        response.getTotalDistanceKm(),
                        response.getTotalTravelTimeMinutes(),
                        response.getPathNodes()
                );

        routeHistoryRepository.save(history);
    }

    // =========================================================
    // VALIDATE ALL COORDINATES
    // =========================================================

    private void validateCoordinates(
            double startLatitude,
            double startLongitude,
            double endLatitude,
            double endLongitude) {

        validateLatitude(
                startLatitude,
                "startLatitude"
        );

        validateLongitude(
                startLongitude,
                "startLongitude"
        );

        validateLatitude(
                endLatitude,
                "endLatitude"
        );

        validateLongitude(
                endLongitude,
                "endLongitude"
        );
    }

    // =========================================================
    // VALIDATE ALGORITHM
    // =========================================================

    private void validateAlgorithm(
            String algorithm) {

        if (!algorithm.equalsIgnoreCase("dijkstra")
                && !algorithm.equalsIgnoreCase("astar")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid algorithm. Use 'dijkstra' or 'astar'."
            );
        }
    }

    // =========================================================
    // VALIDATE ROUTE RESULT
    // =========================================================

    private void validateRouteResult(
            RouteResult route) {

        if (route == null
                || route.getPath() == null
                || route.getPath().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No route found between the given coordinates."
            );
        }
    }

    // =========================================================
    // VALIDATE LATITUDE
    // =========================================================

    private void validateLatitude(
            double latitude,
            String parameterName) {

        if (Double.isNaN(latitude)
                || Double.isInfinite(latitude)
                || latitude < -90
                || latitude > 90) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    parameterName +
                            " must be between -90 and 90."
            );
        }
    }

    // =========================================================
    // VALIDATE LONGITUDE
    // =========================================================

    private void validateLongitude(
            double longitude,
            String parameterName) {

        if (Double.isNaN(longitude)
                || Double.isInfinite(longitude)
                || longitude < -180
                || longitude > 180) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    parameterName +
                            " must be between -180 and 180."
            );
        }
    }
}