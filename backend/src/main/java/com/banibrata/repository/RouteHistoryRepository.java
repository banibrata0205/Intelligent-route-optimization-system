package com.banibrata.repository;

import com.banibrata.entity.RouteHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteHistoryRepository
        extends JpaRepository<RouteHistory, Long> {

    List<RouteHistory> findTop20ByOrderByCreatedAtDesc();
}