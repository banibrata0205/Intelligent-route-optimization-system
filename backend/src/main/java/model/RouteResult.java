package model;

import java.util.List;

public class RouteResult {

    private List<Node> path;
    private double totalDistance;

    public RouteResult(List<Node> path, double totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public List<Node> getPath() {
        return path;
    }

    public double getTotalDistance() {
        return totalDistance;
    }
}