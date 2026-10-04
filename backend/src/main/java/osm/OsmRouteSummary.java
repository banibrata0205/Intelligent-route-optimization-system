package osm;

import model.Node;
import model.RouteResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OsmRouteSummary {

    private final RouteResult route;
    private final List<OsmRoad> roads;
    private final List<OsmRouteSection> sections;

    public OsmRouteSummary(RouteResult route, List<OsmRoad> roads) {
        this.route = route;
        this.roads = roads;
        this.sections = buildSections();
    }

    private List<OsmRouteSection> buildSections() {

        List<OsmRouteSection> result = new ArrayList<>();

        if (route == null || route.getPath() == null || route.getPath().size() < 2) {
            return result;
        }

        if (roads == null || roads.isEmpty()) {
            return result;
        }

        Map<String, OsmRouteSection> sectionMap = new LinkedHashMap<>();

        List<Node> path = route.getPath();

        int segmentCount = Math.min(path.size() - 1, roads.size());

        for (int i = 0; i < segmentCount; i++) {

            Node fromNode = path.get(i);
            Node toNode = path.get(i + 1);

            OsmRoad road = roads.get(i);

            if (road == null) {
                continue;
            }

            /*
             * Calculate the actual distance between the two
             * consecutive route nodes.
             */
            double segmentDistanceKm =
                    OsmRouteDistanceCalculator.calculateSegmentDistanceKm(
                            fromNode,
                            toNode
                    );

            /*
             * Determine the speed used for this road segment.
             *
             * Priority:
             * 1. OSM maxspeed
             * 2. Highway-type fallback speed
             */
            double speedKmh = getSpeedKmh(road);

            /*
             * Travel time = Distance / Speed
             *
             * Distance is in km and speed is in km/h.
             * Multiplying by 60 converts hours into minutes.
             */
            double segmentTravelTimeMinutes =
                    (segmentDistanceKm / speedKmh) * 60.0;

            /*
             * Group segments belonging to the same road.
             */
            String key = getGroupingKey(road);

            OsmRouteSection section = sectionMap.get(key);

            if (section == null) {

                section = new OsmRouteSection(
                        getDisplayRoadName(road),
                        road.getHighwayType(),
                        segmentDistanceKm,
                        segmentTravelTimeMinutes,
                        road.isOneWay(),
                        road.getMaxSpeedKmh()
                );

                sectionMap.put(key, section);

            } else {

                /*
                 * Add this segment's distance and travel time
                 * to the existing grouped road section.
                 */
                section.addDistance(segmentDistanceKm);
                section.addTravelTime(segmentTravelTimeMinutes);

                /*
                 * If the grouped section did not previously have
                 * an OSM maxspeed, but this segment does, preserve it.
                 */
                if (section.getMaxSpeedKmh() == null &&
                        road.getMaxSpeedKmh() != null) {

                    section.setMaxSpeedKmh(
                            road.getMaxSpeedKmh()
                    );
                }
            }
        }

        result.addAll(sectionMap.values());

        return result;
    }

    /**
     * Creates the grouping key for a road.
     *
     * Named roads are normalized so that small differences
     * in spacing/capitalization do not create separate sections.
     *
     * Unnamed roads are grouped using one common key.
     */
    private String getGroupingKey(OsmRoad road) {

        String roadName = road.getRoadName();

        if (roadName == null || roadName.isBlank()) {
            return "unnamed-road";
        }

        return normalizeRoadName(roadName);
    }

    /**
     * Returns the name displayed in the route summary.
     */
    private String getDisplayRoadName(OsmRoad road) {

        String roadName = road.getRoadName();

        if (roadName == null || roadName.isBlank()) {
            return "Unnamed Road";
        }

        return roadName;
    }

    /**
     * Normalizes road names for grouping.
     */
    private String normalizeRoadName(String roadName) {

        return roadName
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", " ");
    }

    /**
     * Determines the speed used for travel-time calculation.
     *
     * First, the actual OSM maxspeed value is used.
     * If it is unavailable, a default speed is selected
     * according to the highway type.
     */
    private double getSpeedKmh(OsmRoad road) {

        if (road.getMaxSpeedKmh() != null &&
                road.getMaxSpeedKmh() > 0) {

            return road.getMaxSpeedKmh();
        }

        return switch (road.getHighwayType()) {

            case "motorway" -> 80.0;
            case "trunk" -> 70.0;
            case "primary" -> 60.0;
            case "secondary" -> 50.0;
            case "tertiary" -> 40.0;
            case "unclassified" -> 35.0;
            case "residential" -> 30.0;
            case "living_street" -> 20.0;
            case "service" -> 20.0;

            default -> 30.0;
        };
    }

    /**
     * Returns the original RouteResult.
     */
    public RouteResult getRoute() {
        return route;
    }

    /**
     * Returns the OSM roads mapped to the route.
     */
    public List<OsmRoad> getRoads() {
        return roads;
    }

    /**
     * Returns grouped route sections.
     */
    public List<OsmRouteSection> getSections() {
        return sections;
    }

    /**
     * Calculates total distance from all grouped sections.
     */
    public double getTotalDistanceKm() {

        double totalDistance = 0.0;

        for (OsmRouteSection section : sections) {
            totalDistance += section.getDistanceKm();
        }

        return totalDistance;
    }

    /**
     * Calculates total travel time from all grouped sections.
     */
    public double getTotalTravelTimeMinutes() {

        double totalTravelTime = 0.0;

        for (OsmRouteSection section : sections) {
            totalTravelTime += section.getTravelTimeMinutes();
        }

        return totalTravelTime;
    }

    /**
     * Returns the number of successfully mapped route segments.
     */
    public int getMappedSegmentCount() {

        if (roads == null) {
            return 0;
        }

        int count = 0;

        for (OsmRoad road : roads) {

            if (road != null) {
                count++;
            }
        }

        return count;
    }

    /**
     * Displays the complete route summary.
     */
    public void display() {

        System.out.println();
        System.out.println("===== ROUTE SUMMARY =====");

        System.out.printf(
                "Total distance: %.2f km%n",
                getTotalDistanceKm()
        );

        System.out.printf(
                "Total travel time: %.2f minutes%n",
                getTotalTravelTimeMinutes()
        );

        System.out.println(
                "Mapped route segments: " + getMappedSegmentCount()
        );

        System.out.println(
                "Grouped route sections: " + sections.size()
        );

        System.out.println();
        System.out.println("===== ROUTE SECTIONS =====");

        for (int i = 0; i < sections.size(); i++) {

            OsmRouteSection section = sections.get(i);

            System.out.printf(
                    "%d. %s%n",
                    i + 1,
                    section
            );
        }

        System.out.println("==========================");
    }
}