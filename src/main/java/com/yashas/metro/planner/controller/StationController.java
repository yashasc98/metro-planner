package com.yashas.metro.planner.controller;

import java.util.List;

import com.yashas.metro.planner.dto.RouteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.yashas.metro.planner.entity.Station;
import com.yashas.metro.planner.service.StationService;
import com.yashas.metro.planner.service.RouteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Stations", description = "Endpoints for station and route queries")
@RestController
@CrossOrigin(origins = "*")
public class StationController {

    private static final Logger logger = LoggerFactory.getLogger(StationController.class);
    private final StationService stationService;
    private final RouteService routeService;

    public StationController(StationService stationService, RouteService routeService) {
        this.stationService = stationService;
        this.routeService = routeService;
    }

    @Operation(summary = "Get all stations", description = "Returns a list of all stations.")
    @GetMapping("/stations")
    public ResponseEntity<List<Station>> getStations() {
        return ResponseEntity.ok(stationService.getStations());
    }

    /**
     * Returns a list of stations matching the given code. Note: Station codes may not be unique.
     */
    @Operation(summary = "Get stations by code", description = "Returns a list of stations matching the given code. Codes may not be unique.")
    @GetMapping("/stations/{code}")
    public ResponseEntity<List<Station>> getStationsByCode(@Parameter(description = "Station code", example = "WHF") @PathVariable String code) {
        return ResponseEntity.ok(stationService.getStationsByCode(code));
    }

    @Operation(summary = "Get stations by line", description = "Returns all stations on a given line.")
    @GetMapping("/lines/{line}/stations")
    public ResponseEntity<List<Station>> getStationsByLine(@Parameter(description = "Line name", example = "Blue") @PathVariable String line) {
        return ResponseEntity.ok(stationService.getStationsByLine(line));
    }

    /**
     * Returns the shortest route between two stations, including the path and total travel time.
     */
    @Operation(summary = "Get shortest route", description = "Returns the shortest route between two stations, including the path and total travel time.")
    @GetMapping("/route/{from}/{to}")
    public ResponseEntity<RouteResult> getShortestRoute(@Parameter(description = "Source station code", example = "WHF") @PathVariable String from, @Parameter(description = "Destination station code", example = "MGD") @PathVariable String to) {
        logger.info("Finding shortest route from {} to {}", from, to);
        RouteResult result = routeService.findShortestPath(from, to);
        if (result.getPath().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

}
