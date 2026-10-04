package com.banibrata.dto;

public class RouteComparisonResponse {

    private double dijkstraDistanceKm;
    private double dijkstraTravelTimeMinutes;
    private int dijkstraPathNodes;

    private double aStarDistanceKm;
    private double aStarTravelTimeMinutes;
    private int aStarPathNodes;

    private double timeDifferenceMinutes;

    public RouteComparisonResponse(
            double dijkstraDistanceKm,
            double dijkstraTravelTimeMinutes,
            int dijkstraPathNodes,
            double aStarDistanceKm,
            double aStarTravelTimeMinutes,
            int aStarPathNodes,
            double timeDifferenceMinutes) {

        this.dijkstraDistanceKm = dijkstraDistanceKm;
        this.dijkstraTravelTimeMinutes = dijkstraTravelTimeMinutes;
        this.dijkstraPathNodes = dijkstraPathNodes;

        this.aStarDistanceKm = aStarDistanceKm;
        this.aStarTravelTimeMinutes = aStarTravelTimeMinutes;
        this.aStarPathNodes = aStarPathNodes;

        this.timeDifferenceMinutes = timeDifferenceMinutes;
    }

    public double getDijkstraDistanceKm() {
        return dijkstraDistanceKm;
    }

    public double getDijkstraTravelTimeMinutes() {
        return dijkstraTravelTimeMinutes;
    }

    public int getDijkstraPathNodes() {
        return dijkstraPathNodes;
    }

    public double getAStarDistanceKm() {
        return aStarDistanceKm;
    }

    public double getAStarTravelTimeMinutes() {
        return aStarTravelTimeMinutes;
    }

    public int getAStarPathNodes() {
        return aStarPathNodes;
    }

    public double getTimeDifferenceMinutes() {
        return timeDifferenceMinutes;
    }
}
