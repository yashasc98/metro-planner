package com.yashas.metro.planner.dto;

import com.yashas.metro.planner.entity.Station;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Result of a route search, including the path and total travel time.")
public class RouteResult {
    @Schema(description = "List of stations in the path, in order.")
    private final List<Station> path;
    @Schema(description = "Total travel time in minutes.")
    private final int totalTime;

    public RouteResult(List<Station> path, int totalTime) {
        this.path = path;
        this.totalTime = totalTime;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getTotalTime() {
        return totalTime;
    }
}

