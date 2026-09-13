package algorithm;

import graph.Graph;
import model.Edge;
import model.Node;
import model.RouteExplanation;
import model.RouteResult;
import model.TrafficLevel;

import java.util.List;

public class RouteExplanationAnalyzer {

    public RouteExplanation explainRoute(
            Graph graph,
            RouteResult selectedRoute,
            RouteResult alternativeRoute) {

        if (selectedRoute.getPath().isEmpty()) {

            return new RouteExplanation(
                    "No valid route was found.",
                    Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY
            );
        }

        double selectedTime =
                selectedRoute
                        .getTotalTravelTimeMinutes();

        double alternativeTime =
                Double.POSITIVE_INFINITY;

        if (alternativeRoute != null
                && !alternativeRoute
                        .getPath()
                        .isEmpty()) {

            alternativeTime =
                    alternativeRoute
                            .getTotalTravelTimeMinutes();
        }

        TrafficLevel highestTraffic =
                TrafficLevel.NORMAL;

        String affectedRoad = "";

        // Find the highest traffic level
        // on the selected route.
        for (int i = 0;
             i < selectedRoute.getPath().size() - 1;
             i++) {

            Node source =
                    selectedRoute
                            .getPath()
                            .get(i);

            Node destination =
                    selectedRoute
                            .getPath()
                            .get(i + 1);

            Edge edge =
                    findEdge(
                            graph,
                            source.getId(),
                            destination.getId()
                    );

            if (edge == null) {
                continue;
            }

            if (edge.getTrafficLevel()
                    .getMultiplier()
                    >
                    highestTraffic
                            .getMultiplier()) {

                highestTraffic =
                        edge.getTrafficLevel();

                affectedRoad =
                        source.getName()
                                + " -> "
                                + destination.getName();
            }
        }

        String reason;

        // ==========================================
        // CREATE EXPLANATION
        // ==========================================

        if (Double.isInfinite(
                alternativeTime)) {

            reason =
                    "No valid alternative route was found.";

        } else if (selectedTime <
                alternativeTime) {

            if (highestTraffic ==
                    TrafficLevel.NORMAL) {

                reason =
                        "The selected route has normal "
                                + "traffic and provides the "
                                + "lowest estimated travel time.";

            } else if (highestTraffic ==
                    TrafficLevel.LIGHT) {

                reason =
                        "The selected route provides the "
                                + "lowest estimated travel time "
                                + "under the current light traffic "
                                + "conditions.";

            } else if (highestTraffic ==
                    TrafficLevel.MODERATE) {

                reason =
                        "The selected route provides the "
                                + "lowest estimated travel time. "
                                + affectedRoad
                                + " has moderate traffic.";

            } else if (highestTraffic ==
                    TrafficLevel.HEAVY) {

                reason =
                        "The selected route provides the "
                                + "lowest estimated travel time "
                                + "despite heavy traffic on "
                                + affectedRoad
                                + ".";

            } else {

                reason =
                        "The selected route provides the "
                                + "lowest estimated travel time "
                                + "despite severe traffic on "
                                + affectedRoad
                                + ".";
            }

        } else {

            reason =
                    "The selected route does not improve "
                            + "travel time over the alternative route.";
        }

        return new RouteExplanation(
                reason,
                selectedTime,
                alternativeTime
        );
    }

    private Edge findEdge(
            Graph graph,
            int sourceId,
            int destinationId) {

        for (Edge edge :
                graph.getNeighbors(sourceId)) {

            if (edge.getDestination()
                    .getId()
                    == destinationId) {

                return edge;
            }
        }

        return null;
    }
}