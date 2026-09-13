package model;

import java.util.List;

public class MultiStopRoute {

    private List<Node> stops;

    private double totalDistanceKm;

    private double totalTravelTimeHours;

    public MultiStopRoute(
            List<Node> stops,
            double totalDistanceKm,
            double totalTravelTimeHours) {

        this.stops = stops;

        this.totalDistanceKm =
                totalDistanceKm;

        this.totalTravelTimeHours =
                totalTravelTimeHours;
    }

    public List<Node> getStops() {
        return stops;
    }

    // Total number of locations in the complete route
    public int getNumberOfLocations() {

        return stops.size();
    }

    // Number of destinations after the starting location
    public int getNumberOfStops() {

        if (stops.isEmpty()) {
            return 0;
        }

        return stops.size() - 1;
    }

    public double getTotalDistanceKm() {

        return totalDistanceKm;
    }

    public double getTotalTravelTimeHours() {

        return totalTravelTimeHours;
    }

    public double getTotalTravelTimeMinutes() {

        return totalTravelTimeHours * 60;
    }

    public void displayRoute() {

        if (stops.isEmpty()) {

            System.out.println(
                    "No route found."
            );

            return;
        }

        for (int i = 0;
             i < stops.size();
             i++) {

            System.out.print(
                    stops.get(i).getName()
            );

            if (i < stops.size() - 1) {

                System.out.print(
                        " -> "
                );
            }
        }

        System.out.println();
    }

    public void displayDetails() {

        if (stops.isEmpty()) {

            System.out.println(
                    "No valid multi-stop route found."
            );

            return;
        }

        System.out.println();
        System.out.println(
                "===== MULTI-STOP ROUTE RESULT ====="
        );

        System.out.println(
                "Route:"
        );

        displayRoute();

        System.out.printf(
                "Start location: %s%n",
                stops.get(0).getName()
        );

        System.out.printf(
                "Final destination: %s%n",
                stops.get(
                        stops.size() - 1
                ).getName()
        );

        System.out.printf(
                "Number of stops: %d%n",
                getNumberOfStops()
        );

        System.out.printf(
                "Total distance: %.2f km%n",
                totalDistanceKm
        );

        System.out.printf(
                "Total travel time: %.2f minutes%n",
                getTotalTravelTimeMinutes()
        );
    }
}