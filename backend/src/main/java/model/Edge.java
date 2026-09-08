package model;

public class Edge {

    private Node source;
    private Node destination;

    // Base road distance
    private double distance;

    // Traffic multiplier
    private double trafficMultiplier;


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

        // Default: normal traffic
        this.trafficMultiplier = 1.0;
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

    public double getTrafficMultiplier() {
        return trafficMultiplier;
    }


    // =========================================
    // SET TRAFFIC
    // =========================================

    public void setTrafficMultiplier(
            double trafficMultiplier) {

        if (trafficMultiplier < 1.0) {
            throw new IllegalArgumentException(
                    "Traffic multiplier cannot be less than 1.0"
            );
        }

        this.trafficMultiplier =
                trafficMultiplier;
    }


    // =========================================
    // EFFECTIVE TRAVEL COST
    // =========================================

    public double getTravelCost() {

        return distance *
                trafficMultiplier;
    }


    // =========================================
    // DISPLAY
    // =========================================

    @Override
    public String toString() {

        return source.getName()
                + " -> "
                + destination.getName()
                + " ("
                + distance
                + " km, traffic x"
                + trafficMultiplier
                + ")";
    }
}