package osm;

import graph.Graph;
import model.Node;

public class OsmNearestNodeFinder {

    public Node findNearestNode(
            Graph graph,
            double latitude,
            double longitude) {

        Node nearestNode = null;

        double minimumDistance =
                Double.POSITIVE_INFINITY;

        for (Node node :
                graph.getAllNodes()) {

            double distance =
                    calculateDistance(
                            latitude,
                            longitude,
                            node.getLatitude(),
                            node.getLongitude()
                    );

            if (distance < minimumDistance) {

                minimumDistance =
                        distance;

                nearestNode =
                        node;
            }
        }

        return nearestNode;
    }

    private double calculateDistance(
            double latitude1,
            double longitude1,
            double latitude2,
            double longitude2) {

        double earthRadiusKm =
                6371.0;

        double lat1 =
                Math.toRadians(latitude1);

        double lat2 =
                Math.toRadians(latitude2);

        double latitudeDifference =
                lat2 - lat1;

        double longitudeDifference =
                Math.toRadians(
                        longitude2 - longitude1
                );

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

        return earthRadiusKm * c;
    }
}