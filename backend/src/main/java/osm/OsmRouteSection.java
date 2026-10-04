package osm;

public class OsmRouteSection {

    private String roadName;

    private String highwayType;

    private double distanceKm;

    private double travelTimeMinutes;

    private boolean oneWay;

    private Double maxSpeedKmh;

    public OsmRouteSection(
            String roadName,
            String highwayType,
            double distanceKm,
            double travelTimeMinutes,
            boolean oneWay,
            Double maxSpeedKmh) {

        this.roadName =
                roadName;

        this.highwayType =
                highwayType;

        this.distanceKm =
                distanceKm;

        this.travelTimeMinutes =
                travelTimeMinutes;

        this.oneWay =
                oneWay;

        this.maxSpeedKmh =
                maxSpeedKmh;
    }

    public String getRoadName() {

        return roadName;
    }

    public String getHighwayType() {

        return highwayType;
    }

    public double getDistanceKm() {

        return distanceKm;
    }

    public double getTravelTimeMinutes() {

        return travelTimeMinutes;
    }

    public boolean isOneWay() {

        return oneWay;
    }

    public Double getMaxSpeedKmh() {

        return maxSpeedKmh;
    }

    public void setMaxSpeedKmh(Double maxSpeedKmh) {
        this.maxSpeedKmh = maxSpeedKmh;
    }

    public void addDistance(
            double additionalDistanceKm) {

        distanceKm +=
                additionalDistanceKm;
    }

    public void addTravelTime(
            double additionalTravelTimeMinutes) {

        travelTimeMinutes +=
                additionalTravelTimeMinutes;
    }

    @Override
    public String toString() {

        String speedText;

        if (maxSpeedKmh == null) {

            speedText =
                    "Not available";

        } else {

            speedText =
                    String.format(
                            "%.1f km/h",
                            maxSpeedKmh
                    );
        }

        return "OsmRouteSection{" +
                "roadName='" +
                roadName + '\'' +
                ", highwayType='" +
                highwayType + '\'' +
                ", distanceKm=" +
                String.format(
                        "%.3f",
                        distanceKm
                ) +
                ", travelTimeMinutes=" +
                String.format(
                        "%.3f",
                        travelTimeMinutes
                ) +
                ", oneWay=" +
                oneWay +
                ", maxSpeed=" +
                speedText +
                '}';
    }
}