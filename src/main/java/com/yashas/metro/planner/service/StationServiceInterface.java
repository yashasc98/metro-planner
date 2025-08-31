package com.yashas.metro.planner.service;

import java.util.List;

import com.yashas.metro.planner.entity.Station;

public interface StationServiceInterface {

    /**
     * Retrieves all stations.
     *
     * @return a list of all stations
     */
    List<Station> getStations();

    /**
     * Retrieves stations belonging to a specific metro line.
     *
     * @param line the name or code of the metro line
     * @return a list of stations on the specified line
     */
    List<Station> getStationsByLine(String line);

    /**
     * Retrieves stations by their unique code.
     *
     * @param code the unique station code
     * @return a list of stations matching the code
     */
    List<Station> getStationsByCode(String code);

}
