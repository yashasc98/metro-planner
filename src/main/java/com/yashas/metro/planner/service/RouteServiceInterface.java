package com.yashas.metro.planner.service;

import com.yashas.metro.planner.dto.RouteResult;

public interface RouteServiceInterface {
    /**
     * Finds the shortest path between two station codes using Dijkstra's algorithm.
     *
     * @param fromCode the code of the starting station
     * @param toCode   the code of the destination station
     * @return RouteResult containing the path and total travel time
     */
    RouteResult findShortestPath(String fromCode, String toCode);
}

