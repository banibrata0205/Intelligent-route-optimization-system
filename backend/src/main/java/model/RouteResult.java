package model;

import java.util.List;

public class RouteResult {

    private List<Node> path;

    // Total travel time in hours
    private double totalTravelTimeHours;


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public RouteResult(
            List<Node> path,
            double totalTravelTimeHours) {

        this.path = path;
        this.totalTravelTimeHours =
                totalTravelTimeHours;
    }


    // =========================================
    // GET PATH
    // =========================================

    public List<Node> getPath() {

        return path;
    }


    // =========================================
    // GET TRAVEL TIME IN HOURS
    // =========================================

    public double getTotalTravelTimeHours() {

        return totalTravelTimeHours;
    }


    // =========================================
    // GET TRAVEL TIME IN MINUTES
    // =========================================

    public double getTotalTravelTimeMinutes() {

        return totalTravelTimeHours * 60;
    }
}