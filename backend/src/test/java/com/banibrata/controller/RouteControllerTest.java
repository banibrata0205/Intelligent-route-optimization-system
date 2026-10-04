package com.banibrata.controller;

import com.banibrata.repository.RouteHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import osm.OsmRouteService;

import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RouteControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        OsmRouteService routeService =
                mock(OsmRouteService.class);

        RouteHistoryRepository repository =
                mock(RouteHistoryRepository.class);

        RouteController controller =
                new RouteController(
                        routeService,
                        repository
                );

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(controller)
                        .build();
    }

    @Test
    void healthEndpointShouldReturnSuccess() throws Exception {

        mockMvc.perform(
                get("/api/health")
        )
        .andExpect(
                status().isOk()
        );
    }

    @Test
    void invalidAlgorithmShouldReturnBadRequest()
            throws Exception {

        mockMvc.perform(
                get("/api/route")
                        .param(
                                "startLatitude",
                                "22.5726"
                        )
                        .param(
                                "startLongitude",
                                "88.3639"
                        )
                        .param(
                                "endLatitude",
                                "22.6500"
                        )
                        .param(
                                "endLongitude",
                                "88.4460"
                        )
                        .param(
                                "algorithm",
                                "invalid"
                        )
        )
        .andExpect(
                status().isBadRequest()
        );
    }

    @Test
    void invalidLatitudeShouldReturnBadRequest()
            throws Exception {

        mockMvc.perform(
                get("/api/route")
                        .param(
                                "startLatitude",
                                "200"
                        )
                        .param(
                                "startLongitude",
                                "88.3639"
                        )
                        .param(
                                "endLatitude",
                                "22.6500"
                        )
                        .param(
                                "endLongitude",
                                "88.4460"
                        )
                        .param(
                                "algorithm",
                                "dijkstra"
                        )
        )
        .andExpect(
                status().isBadRequest()
        );
    }
}
