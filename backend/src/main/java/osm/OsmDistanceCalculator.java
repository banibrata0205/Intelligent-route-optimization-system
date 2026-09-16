package osm;

import org.openstreetmap.osmosis.core.domain.v0_6.Node;

public class OsmDistanceCalculator {

    private static final double EARTH_RADIUS_KM =
            6371.0;

    public static double calculateDistance(
            Node node1,
            Node node2) {

        double latitude1 =
                Math.toRadians(
                        node1.getLatitude()
                );

        double longitude1 =
                Math.toRadians(
                        node1.getLongitude()
                );

        double latitude2 =
                Math.toRadians(
                        node2.getLatitude()
                );

        double longitude2 =
                Math.toRadians(
                        node2.getLongitude()
                );

        double latitudeDifference =
                latitude2 - latitude1;

        double longitudeDifference =
                longitude2 - longitude1;

        double a =
                Math.sin(latitudeDifference / 2)
                        * Math.sin(latitudeDifference / 2)
                +
                Math.cos(latitude1)
                        * Math.cos(latitude2)
                        * Math.sin(longitudeDifference / 2)
                        * Math.sin(longitudeDifference / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return EARTH_RADIUS_KM * c;
    }
}