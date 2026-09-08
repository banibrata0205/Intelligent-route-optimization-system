package model;
public class Edge {

    private Node source;
    private Node destination;
    private double distance;

    public Edge(Node source, Node destination, double distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return source.getName() +
                " -> " +
                destination.getName() +
                " (" +
                distance +
                " km)";
    }
}