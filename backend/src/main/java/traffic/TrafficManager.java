package traffic;

import model.Edge;
import model.TrafficLevel;

public class TrafficManager {

    public void updateRoadTraffic(
            Edge edge,
            TrafficLevel newTrafficLevel) {

        edge.updateTraffic(
                newTrafficLevel
        );
    }
}