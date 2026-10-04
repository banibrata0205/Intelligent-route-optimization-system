package com.banibrata.dto;

import java.util.List;

public class RouteResponse {

    private String algorithm;
    private double totalDistanceKm;
    private double totalTravelTimeHours;
    private double totalTravelTimeMinutes;
    private int pathNodes;
    private List<RoutePoint> routePoints;

    public RouteResponse(
            String algorithm,
            double totalDistanceKm,
            double totalTravelTimeHours,
            double totalTravelTimeMinutes,
            int pathNodes,
            List<RoutePoint> routePoints) {

        this.algorithm = algorithm;
        this.totalDistanceKm = totalDistanceKm;
        this.totalTravelTimeHours = totalTravelTimeHours;
        this.totalTravelTimeMinutes = totalTravelTimeMinutes;
        this.pathNodes = pathNodes;
        this.routePoints = routePoints;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public double getTotalTravelTimeHours() {
        return totalTravelTimeHours;
    }

    public double getTotalTravelTimeMinutes() {
        return totalTravelTimeMinutes;
    }

    public int getPathNodes() {
        return pathNodes;
    }

    public List<RoutePoint> getRoutePoints() {
        return routePoints;
    }
}