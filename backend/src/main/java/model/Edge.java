package model;

public class Edge {

    private Node source;
    private Node destination;

    // Base road distance
    private double distance;

    // Current traffic condition
    private TrafficLevel trafficLevel;


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public Edge(
            Node source,
            Node destination,
            double distance) {

        this.source = source;
        this.destination = destination;
        this.distance = distance;

        // Default traffic condition
        this.trafficLevel =
                TrafficLevel.NORMAL;
    }


    // =========================================
    // GETTERS
    // =========================================

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    public TrafficLevel getTrafficLevel() {
        return trafficLevel;
    }


    // =========================================
    // SET TRAFFIC LEVEL
    // =========================================

    public void setTrafficLevel(
            TrafficLevel trafficLevel) {

        this.trafficLevel =
                trafficLevel;
    }


    // =========================================
    // CALCULATE TRAVEL COST
    // =========================================

    public double getTravelCost() {

        return distance *
                trafficLevel.getMultiplier();
    }


    // =========================================
    // DISPLAY EDGE
    // =========================================

    @Override
    public String toString() {

        return source.getName()
                + " -> "
                + destination.getName()
                + " ("
                + distance
                + " km, traffic="
                + trafficLevel
                + ", cost="
                + getTravelCost()
                + ")";
    }
}