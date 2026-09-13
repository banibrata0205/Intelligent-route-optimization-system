package model;

public class RouteExplanation {

    private String reason;

    private double selectedRouteTimeMinutes;

    private double alternativeRouteTimeMinutes;

    private double timeSavedMinutes;

    public RouteExplanation(
            String reason,
            double selectedRouteTimeMinutes,
            double alternativeRouteTimeMinutes) {

        this.reason = reason;

        this.selectedRouteTimeMinutes =
                selectedRouteTimeMinutes;

        this.alternativeRouteTimeMinutes =
                alternativeRouteTimeMinutes;

        this.timeSavedMinutes =
                alternativeRouteTimeMinutes
                        - selectedRouteTimeMinutes;
    }

    public String getReason() {

        return reason;
    }

    public double getSelectedRouteTimeMinutes() {

        return selectedRouteTimeMinutes;
    }

    public double getAlternativeRouteTimeMinutes() {

        return alternativeRouteTimeMinutes;
    }

    public double getTimeSavedMinutes() {

        return timeSavedMinutes;
    }

    public void displayExplanation() {

        System.out.println();
        System.out.println(
                "===== ROUTE EXPLANATION ====="
        );

        System.out.println(
                "Reason: " + reason
        );

        System.out.printf(
                "Selected route time: %.2f minutes%n",
                selectedRouteTimeMinutes
        );

        if (Double.isInfinite(
                alternativeRouteTimeMinutes)) {

            System.out.println(
                    "Alternative route time: "
                            + "No valid alternative"
            );

        } else {

            System.out.printf(
                    "Alternative route time: %.2f minutes%n",
                    alternativeRouteTimeMinutes
            );

            System.out.printf(
                    "Time saved: %.2f minutes%n",
                    timeSavedMinutes
            );
        }
    }
}