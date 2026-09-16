package osm;

import java.util.ArrayList;
import java.util.List;

public class OsmRoad {

    private long osmWayId;

    private String highwayType;

    private String roadName;

    private List<Long> nodeIds;

    private double totalDistanceKm;

    private boolean oneWay;

    public OsmRoad(
            long osmWayId,
            String highwayType,
            String roadName,
            List<Long> nodeIds,
            double totalDistanceKm,
            boolean oneWay) {

        this.osmWayId = osmWayId;

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

    @Override
    public String toString() {

        return "OsmRoad{" +
                "osmWayId=" + osmWayId +
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
                '}';
    }
}