package com.banibrata.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "route_history")
public class RouteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double startLatitude;
    private double startLongitude;

    private double endLatitude;
    private double endLongitude;

    private String algorithm;

    private double totalDistanceKm;

    private double totalTravelTimeMinutes;

    private int pathNodes;

    private LocalDateTime createdAt;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public RouteHistory() {
    }

    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public RouteHistory(
            double startLatitude,
            double startLongitude,
            double endLatitude,
            double endLongitude,
            String algorithm,
            double totalDistanceKm,
            double totalTravelTimeMinutes,
            int pathNodes) {

        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.algorithm = algorithm;
        this.totalDistanceKm = totalDistanceKm;
        this.totalTravelTimeMinutes = totalTravelTimeMinutes;
        this.pathNodes = pathNodes;
        this.createdAt = LocalDateTime.now();
    }

    // =========================================================
    // PRE-PERSIST
    // =========================================================

    @PrePersist
    public void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public double getTotalTravelTimeMinutes() {
        return totalTravelTimeMinutes;
    }

    public int getPathNodes() {
        return pathNodes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}