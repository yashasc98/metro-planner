package com.yashas.metro.planner.service;

import java.util.List;

import com.yashas.metro.planner.entity.Station;

public interface StationServiceInterface {

	List<Station> getStations();

	List<Station> getStationsByLine(String line);

	List<Station> getStationsByCode(String code);

}
