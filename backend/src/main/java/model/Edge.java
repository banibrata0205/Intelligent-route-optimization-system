package model;

public class Edge {

    private Node source;
    private Node destination;

    // Base road distance in kilometers
    private double distance;

    // Speed limit in km/h
    private double speedKmh;

    // Current traffic condition
    private TrafficLevel trafficLevel;


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public Edge(
            Node source,
            Node destination,
            double distance,
            double speedKmh) {

        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.speedKmh = speedKmh;

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

    public double getSpeedKmh() {

        return speedKmh;
    }

    public TrafficLevel getTrafficLevel() {

        return trafficLevel;
    }


    // =========================================
    // SET TRAFFIC LEVEL
    // =========================================

    public void setTrafficLevel(
            TrafficLevel trafficLevel) {

        if (trafficLevel == null) {

            throw new IllegalArgumentException(
                    "Traffic level cannot be null"
            );
        }

        this.trafficLevel =
                trafficLevel;
    }


    // =========================================
    // UPDATE TRAFFIC
    // =========================================

    public void updateTraffic(
            TrafficLevel newTrafficLevel) {

        setTrafficLevel(
                newTrafficLevel
        );
    }


    // =========================================
    // CALCULATE EFFECTIVE SPEED
    // =========================================

    public double getEffectiveSpeedKmh() {

        return speedKmh /
                trafficLevel.getMultiplier();
    }


    // =========================================
    // CALCULATE TRAVEL TIME IN HOURS
    // =========================================

    public double getTravelTimeHours() {

        return distance /
                getEffectiveSpeedKmh();
    }


    // =========================================
    // CALCULATE TRAVEL TIME IN MINUTES
    // =========================================

    public double getTravelTimeMinutes() {

        return getTravelTimeHours() * 60;
    }


    // =========================================
    // CALCULATE TRAFFIC-ADJUSTED COST
    // =========================================

    public double getTravelCost() {

        return distance *
                trafficLevel.getMultiplier();
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
                + " km, speed="
                + speedKmh
                + " km/h, traffic="
                + trafficLevel
                + ", cost="
                + String.format(
                        "%.2f",
                        getTravelCost()
                )
                + ", time="
                + String.format(
                        "%.2f",
                        getTravelTimeMinutes()
                )
                + " min)";
    }
}