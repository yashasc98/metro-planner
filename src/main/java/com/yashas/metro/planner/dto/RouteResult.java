package com.yashas.metro.planner.dto;

import com.yashas.metro.planner.entity.Station;

import java.util.List;

public record RouteResult(List<Station> path, int totalTime) {

}

