package com.yashas.metro.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yashas.metro.planner.entity.Station;
import com.yashas.metro.planner.repo.StationRepo;

@Service
public class StationService implements StationServiceInterface {

    private final StationRepo stationRepo;

    public StationService(StationRepo stationRepo) {
        this.stationRepo = stationRepo;
    }

    @Override
    public List<Station> getStations() {
        return stationRepo.findAll();
    }

    @Override
    public List<Station> getStationsByLine(String line) {
        return stationRepo.findByLine(line);
    }

    @Override
    public List<Station> getStationsByCode(String code) {
        return stationRepo.findByCode(code);
    }
}
