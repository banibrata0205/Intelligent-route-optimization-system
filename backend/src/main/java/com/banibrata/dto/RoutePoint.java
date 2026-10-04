package com.banibrata.dto;

public class RoutePoint {

    private int nodeId;
    private double latitude;
    private double longitude;

    public RoutePoint(
            int nodeId,
            double latitude,
            double longitude) {

        this.nodeId = nodeId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getNodeId() {
        return nodeId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
