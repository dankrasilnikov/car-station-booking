package com.zephyra.station.controllers;

import com.zephyra.station.dto.StationCreateDTO;
import com.zephyra.station.models.Station;
import com.zephyra.station.repository.StationRepository;
import com.zephyra.station.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    @Autowired
    StationRepository gasStationRepository;
    @Autowired
    StationService stationService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/stations/add")
    public ResponseEntity<String> addStation(@RequestBody StationCreateDTO dto) {
        stationService.addStation(dto);
        return ResponseEntity.ok("Station with " + dto.getConnectorCount() + " connectors created");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/stations/delete/{id}")
    public ResponseEntity<String> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.ok("Station deleted");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stations")
    public ResponseEntity<List<Station>> getAllStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }
}