package osm;

import model.Node;

import java.util.List;

public class OsmRouteDistanceCalculator {

    public static double calculateDistanceKm(
            List<Node> path) {

        if (path == null
                || path.size() < 2) {

            return 0.0;
        }

        double totalDistanceKm = 0.0;

        for (int i = 0;
             i < path.size() - 1;
             i++) {

            Node currentNode =
                    path.get(i);

            Node nextNode =
                    path.get(i + 1);

            totalDistanceKm +=
                    calculateSegmentDistanceKm(
                            currentNode,
                            nextNode
                    );
        }

        return totalDistanceKm;
    }

    // ---------------------------------------------------------
    // Calculate one route segment
    // ---------------------------------------------------------

    public static double calculateSegmentDistanceKm(
            Node node1,
            Node node2) {

        double earthRadiusKm =
                6371.0;

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
                Math.sin(
                        latitudeDifference / 2
                )
                *
                Math.sin(
                        latitudeDifference / 2
                )
                +
                Math.cos(latitude1)
                *
                Math.cos(latitude2)
                *
                Math.sin(
                        longitudeDifference / 2
                )
                *
                Math.sin(
                        longitudeDifference / 2
                );

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return earthRadiusKm * c;
    }
}