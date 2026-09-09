package model;

public enum TrafficLevel {

    NORMAL(1.0),
    LIGHT(1.2),
    MODERATE(1.5),
    HEAVY(2.0),
    SEVERE(3.0);

    private final double multiplier;

    TrafficLevel(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}