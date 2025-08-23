package com.yashas.metro.planner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.yashas.metro.planner.entity.Station;
import com.yashas.metro.planner.service.StationService;

@RestController
public class StationController {

	@Autowired
	private StationService stationService;

	@GetMapping("/stations")
	public ResponseEntity<List<Station>> getStations() {
		return ResponseEntity.ok(stationService.getStations());
	}

	@GetMapping("/stations/{code}")
	public ResponseEntity<List<Station>> getStationsByCodee(@PathVariable String code) {
		return ResponseEntity.ok(stationService.getStationsByCode(code));
	}

	@GetMapping("/lines/{line}/stations")
	public ResponseEntity<List<Station>> getStationsByLine(@PathVariable String line) {
		return ResponseEntity.ok(stationService.getStationsByLine(line));
	}

}
