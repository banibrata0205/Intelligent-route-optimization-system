package osm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.pbf2.v0_6.PbfReader;

public class OsmPbfReader {

    // ---------------------------------------------------------
    // Kolkata working area
    // ---------------------------------------------------------

    private static final double MIN_LATITUDE = 22.45;
    private static final double MAX_LATITUDE = 22.75;

    private static final double MIN_LONGITUDE = 88.20;
    private static final double MAX_LONGITUDE = 88.55;

    // ---------------------------------------------------------
    // OSM entity counters
    // ---------------------------------------------------------

    private long nodeCount = 0;
    private long wayCount = 0;
    private long relationCount = 0;

    // ---------------------------------------------------------
    // Kolkata filtering counters
    // ---------------------------------------------------------

    private long nodesInsideKolkata = 0;
    private long roadWayCount = 0;
    private long kolkataRoadWayCount = 0;

    // ---------------------------------------------------------
    // Store only Kolkata-area OSM nodes
    // ---------------------------------------------------------

    private final Map<Long, Node> nodeStore =
            new HashMap<>();

    private final Set<Long> kolkataNodeIds =
            new HashSet<>();

    // ---------------------------------------------------------
    // Extracted OSM roads
    // ---------------------------------------------------------

    private final List<OsmRoad> osmRoads =
            new ArrayList<>();

    // ---------------------------------------------------------
    // Read OSM PBF file
    // ---------------------------------------------------------

    public void read(String filePath) {

        File file =
                new File(filePath);

        if (!file.exists()) {

            throw new IllegalArgumentException(
                    "OSM file not found: "
                            + filePath
            );
        }

        System.out.println(
                "Reading OSM file:"
        );

        System.out.println(
                file.getAbsolutePath()
        );

        PbfReader reader =
                new PbfReader(
                        file,
                        4
                );

        reader.setSink(
                new Sink() {

                    @Override
                    public void initialize(
                            Map<String, Object> metaData) {

                        System.out.println(
                                "OSM reader initialized."
                        );
                    }

                    @Override
                    public void process(
                            EntityContainer entityContainer) {

                        switch (
                                entityContainer
                                        .getEntity()
                                        .getType()
                        ) {

                            case Node:

                                processNode(
                                        (Node)
                                                entityContainer
                                                        .getEntity()
                                );

                                break;

                            case Way:

                                processWay(
                                        (Way)
                                                entityContainer
                                                        .getEntity()
                                );

                                break;

                            case Relation:

                                relationCount++;

                                break;

                            default:

                                break;
                        }
                    }

                    @Override
                    public void complete() {

                        System.out.println();

                        System.out.println(
                                "===== OSM DATA SUMMARY ====="
                        );

                        System.out.println(
                                "Total nodes: "
                                        + nodeCount
                        );

                        System.out.println(
                                "Total ways: "
                                        + wayCount
                        );

                        System.out.println(
                                "Total relations: "
                                        + relationCount
                        );

                        System.out.println(
                                "Nodes inside Kolkata area: "
                                        + nodesInsideKolkata
                        );

                        System.out.println(
                                "Drivable road ways: "
                                        + roadWayCount
                        );

                        System.out.println(
                                "Kolkata-area road ways: "
                                        + kolkataRoadWayCount
                        );

                        System.out.println(
                                "Stored Kolkata nodes: "
                                        + nodeStore.size()
                        );

                        System.out.println(
                                "Extracted OSM roads: "
                                        + osmRoads.size()
                        );

                        System.out.println();

                        System.out.println(
                                "OSM reading completed."
                        );
                    }

                    @Override
                    public void close() {

                        System.out.println(
                                "OSM reader closed."
                        );
                    }
                }
        );

        reader.run();
    }

    // ---------------------------------------------------------
    // Process OSM node
    // ---------------------------------------------------------

    private void processNode(
            Node node) {

        nodeCount++;

        double latitude =
                node.getLatitude();

        double longitude =
                node.getLongitude();

        if (isInsideKolkata(
                latitude,
                longitude
        )) {

            nodesInsideKolkata++;

            kolkataNodeIds.add(
                    node.getId()
            );

            nodeStore.put(
                    node.getId(),
                    node
            );
        }
    }

    // ---------------------------------------------------------
    // Process OSM way
    // ---------------------------------------------------------

    private void processWay(
            Way way) {

        wayCount++;

        String highwayType =
                getHighwayType(way);

        if (!isDrivableRoad(
                highwayType
        )) {

            return;
        }

        roadWayCount++;

        if (!hasNodeInsideKolkata(
                way
        )) {

            return;
        }

        kolkataRoadWayCount++;

        String roadName =
                getRoadName(way);

        boolean oneWay =
                isOneWay(way);

        Double maxSpeedKmh =
                getMaxSpeedKmh(way);

        List<Long> nodeIds =
                new ArrayList<>();

        for (var wayNode :
                way.getWayNodes()) {

            long nodeId =
                    wayNode.getNodeId();

            if (nodeStore.containsKey(
                    nodeId
            )) {

                nodeIds.add(
                        nodeId
                );
            }
        }

        if (nodeIds.size() < 2) {

            return;
        }

        double totalDistanceKm =
                calculateRoadDistance(
                        nodeIds
                );

        OsmRoad road =
                new OsmRoad(
                        way.getId(),
                        highwayType,
                        roadName,
                        nodeIds,
                        totalDistanceKm,
                        oneWay,
                        maxSpeedKmh
                );

        osmRoads.add(
                road
        );
    }

    // ---------------------------------------------------------
    // Calculate total road distance
    // ---------------------------------------------------------

    private double calculateRoadDistance(
            List<Long> nodeIds) {

        double totalDistanceKm = 0.0;

        for (int i = 0;
             i < nodeIds.size() - 1;
             i++) {

            Node currentNode =
                    nodeStore.get(
                            nodeIds.get(i)
                    );

            Node nextNode =
                    nodeStore.get(
                            nodeIds.get(i + 1)
                    );

            if (currentNode == null
                    || nextNode == null) {

                continue;
            }

            totalDistanceKm +=
                    OsmDistanceCalculator
                            .calculateDistance(
                                    currentNode,
                                    nextNode
                            );
        }

        return totalDistanceKm;
    }

    // ---------------------------------------------------------
    // Get highway type
    // ---------------------------------------------------------

    private String getHighwayType(
            Way way) {

        for (Tag tag :
                way.getTags()) {

            if ("highway".equals(
                    tag.getKey()
            )) {

                return tag.getValue();
            }
        }

        return null;
    }

    // ---------------------------------------------------------
    // Get road name
    // ---------------------------------------------------------

    private String getRoadName(
            Way way) {

        for (Tag tag :
                way.getTags()) {

            if ("name".equals(
                    tag.getKey()
            )) {

                return tag.getValue();
            }
        }

        return "Unnamed Road";
    }

    // ---------------------------------------------------------
    // Detect one-way road
    // ---------------------------------------------------------

    private boolean isOneWay(
            Way way) {

        for (Tag tag :
                way.getTags()) {

            if ("oneway".equals(
                    tag.getKey()
            )) {

                String value =
                        tag.getValue();

                return "yes".equalsIgnoreCase(value)
                        || "true".equalsIgnoreCase(value)
                        || "1".equals(value);
            }
        }

        return false;
    }

    // ---------------------------------------------------------
    // Read maxspeed tag
    // ---------------------------------------------------------

    private Double getMaxSpeedKmh(
            Way way) {

        for (Tag tag :
                way.getTags()) {

            if ("maxspeed".equals(
                    tag.getKey()
            )) {

                return parseMaxSpeed(
                        tag.getValue()
                );
            }
        }

        return null;
    }

    // ---------------------------------------------------------
    // Convert maxspeed value to km/h
    // ---------------------------------------------------------

    private Double parseMaxSpeed(
            String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return null;
        }

        String speed =
                value.trim()
                        .toLowerCase();

        try {

            // Example: "50"
            if (speed.matches(
                    "\\d+(\\.\\d+)?"
            )) {

                return Double.parseDouble(
                        speed
                );
            }

            // Example: "50 km/h"
            if (speed.contains(
                    "km/h"
            )) {

                String number =
                        speed.replace(
                                "km/h",
                                ""
                        ).trim();

                return Double.parseDouble(
                        number
                );
            }

            // Example: "30 mph"
            if (speed.contains(
                    "mph"
            )) {

                String number =
                        speed.replace(
                                "mph",
                                ""
                        ).trim();

                double mph =
                        Double.parseDouble(
                                number
                        );

                return mph * 1.60934;
            }

        } catch (NumberFormatException e) {

            return null;
        }

        return null;
    }

    // ---------------------------------------------------------
    // Check whether road touches
    // Kolkata working area
    // ---------------------------------------------------------

    private boolean hasNodeInsideKolkata(
            Way way) {

        for (var wayNode :
                way.getWayNodes()) {

            if (kolkataNodeIds.contains(
                    wayNode.getNodeId()
            )) {

                return true;
            }
        }

        return false;
    }

    // ---------------------------------------------------------
    // Check geographic bounding box
    // ---------------------------------------------------------

    private boolean isInsideKolkata(
            double latitude,
            double longitude) {

        return latitude >= MIN_LATITUDE
                && latitude <= MAX_LATITUDE
                && longitude >= MIN_LONGITUDE
                && longitude <= MAX_LONGITUDE;
    }

    // ---------------------------------------------------------
    // Check drivable road type
    // ---------------------------------------------------------

    private boolean isDrivableRoad(
            String highwayType) {

        if (highwayType == null) {

            return false;
        }

        switch (highwayType) {

            case "motorway":
            case "trunk":
            case "primary":
            case "secondary":
            case "tertiary":
            case "unclassified":
            case "residential":
            case "living_street":
            case "service":

                return true;

            default:

                return false;
        }
    }

    // ---------------------------------------------------------
    // Return extracted OSM roads
    // ---------------------------------------------------------

    public List<OsmRoad> getOsmRoads() {

        return osmRoads;
    }

    // ---------------------------------------------------------
    // Return stored Kolkata OSM nodes
    // ---------------------------------------------------------

    public Map<Long, Node> getNodeStore() {

        return nodeStore;
    }
}