package algorithm;

import model.Node;

public class GeoUtils {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double calculateDistance(
            Node node1,
            Node node2) {

        double lat1 = Math.toRadians(
                node1.getLatitude()
        );

        double lon1 = Math.toRadians(
                node1.getLongitude()
        );

        double lat2 = Math.toRadians(
                node2.getLatitude()
        );

        double lon2 = Math.toRadians(
                node2.getLongitude()
        );

        double latitudeDifference =
                lat2 - lat1;

        double longitudeDifference =
                lon2 - lon1;

        double a =
                Math.sin(latitudeDifference / 2)
                        * Math.sin(latitudeDifference / 2)
                +
                Math.cos(lat1)
                        * Math.cos(lat2)
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