package model;

public class RouteComparison {

    private RouteResult selectedRoute;
    private RouteResult alternativeRoute;


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public RouteComparison(
            RouteResult selectedRoute,
            RouteResult alternativeRoute) {

        this.selectedRoute = selectedRoute;
        this.alternativeRoute = alternativeRoute;
    }


    // =========================================
    // GET SELECTED ROUTE
    // =========================================

    public RouteResult getSelectedRoute() {

        return selectedRoute;
    }


    // =========================================
    // GET ALTERNATIVE ROUTE
    // =========================================

    public RouteResult getAlternativeRoute() {

        return alternativeRoute;
    }


    // =========================================
    // CALCULATE TIME SAVED
    // =========================================

    public double getTimeSavedMinutes() {

        return alternativeRoute
                .getTotalTravelTimeMinutes()
                -
                selectedRoute
                        .getTotalTravelTimeMinutes();
    }
}