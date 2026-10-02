package osm;

import java.util.ArrayList;
import java.util.List;

public class OsmRoad {

    // OSM Way ID
    private long osmWayId;

    // Road classification
    private String highwayType;

    // Road name
    private String roadName;

    // Ordered OSM node IDs
    private List<Long> nodeIds;

    // Total road length in kilometers
    private double totalDistanceKm;

    // Whether the road is one-way
    private boolean oneWay;

    // OSM speed limit in km/h
    // null means maxspeed was not available
    private Double maxSpeedKmh;

    public OsmRoad(
            long osmWayId,
            String highwayType,
            String roadName,
            List<Long> nodeIds,
            double totalDistanceKm,
            boolean oneWay,
            Double maxSpeedKmh) {

        this.osmWayId =
                osmWayId;

        this.highwayType =
                highwayType;

        this.roadName =
                roadName;

        this.nodeIds =
                new ArrayList<>(nodeIds);

        this.totalDistanceKm =
                totalDistanceKm;

        this.oneWay =
                oneWay;

        this.maxSpeedKmh =
                maxSpeedKmh;
    }

    public long getOsmWayId() {

        return osmWayId;
    }

    public String getHighwayType() {

        return highwayType;
    }

    public String getRoadName() {

        return roadName;
    }

    public List<Long> getNodeIds() {

        return nodeIds;
    }

    public double getTotalDistanceKm() {

        return totalDistanceKm;
    }

    public boolean isOneWay() {

        return oneWay;
    }

    public Double getMaxSpeedKmh() {

        return maxSpeedKmh;
    }

    public boolean hasMaxSpeed() {

        return maxSpeedKmh != null;
    }

    @Override
    public String toString() {

        String speedText;

        if (maxSpeedKmh == null) {

            speedText =
                    "not available";

        } else {

            speedText =
                    String.format(
                            "%.1f km/h",
                            maxSpeedKmh
                    );
        }

        return "OsmRoad{" +
                "osmWayId=" +
                osmWayId +
                ", highwayType='" +
                highwayType + '\'' +
                ", roadName='" +
                roadName + '\'' +
                ", nodeCount=" +
                nodeIds.size() +
                ", totalDistanceKm=" +
                String.format(
                        "%.3f",
                        totalDistanceKm
                ) +
                ", oneWay=" +
                oneWay +
                ", maxSpeed=" +
                speedText +
                '}';
    }
}