package com.banibrata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import traffic.TrafficManager;

@Configuration
public class TrafficConfig {

    @Bean
    public TrafficManager trafficManager() {
        return new TrafficManager();
    }
}
