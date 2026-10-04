package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EdgeTest {

    @Test
    void normalTrafficShouldKeepOriginalSpeed() {

        Node source =
                new Node(1, "Source", 22.57, 88.36);

        Node destination =
                new Node(2, "Destination", 22.58, 88.37);

        Edge edge =
                new Edge(
                        source,
                        destination,
                        10.0,
                        60.0
                );

        assertEquals(
                TrafficLevel.NORMAL,
                edge.getTrafficLevel()
        );

        assertEquals(
                60.0,
                edge.getEffectiveSpeedKmh(),
                0.001
        );
    }

    @Test
    void heavyTrafficShouldReduceEffectiveSpeed() {

        Node source =
                new Node(1, "Source", 22.57, 88.36);

        Node destination =
                new Node(2, "Destination", 22.58, 88.37);

        Edge edge =
                new Edge(
                        source,
                        destination,
                        10.0,
                        60.0
                );

        edge.updateTraffic(
                TrafficLevel.HEAVY
        );

        assertEquals(
                TrafficLevel.HEAVY,
                edge.getTrafficLevel()
        );

        assertEquals(
                30.0,
                edge.getEffectiveSpeedKmh(),
                0.001
        );

        assertEquals(
                20.0,
                edge.getTravelTimeMinutes(),
                0.001
        );
    }

    @Test
    void severeTrafficShouldIncreaseTravelCost() {

        Node source =
                new Node(1, "Source", 22.57, 88.36);

        Node destination =
                new Node(2, "Destination", 22.58, 88.37);

        Edge edge =
                new Edge(
                        source,
                        destination,
                        5.0,
                        50.0
                );

        edge.updateTraffic(
                TrafficLevel.SEVERE
        );

        assertEquals(
                15.0,
                edge.getTravelCost(),
                0.001
        );
    }
}
