package com.zephyra.station.controllers;

import com.zephyra.station.models.GasStation;
import com.zephyra.station.repository.GasStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private GasStationRepository gasStationRepository;

    @PostMapping("/stations/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addGasStation(@RequestBody GasStation gasStation) {
        gasStationRepository.save(gasStation);
        return ResponseEntity.ok("Gas station added");
    }

    @DeleteMapping("/stations/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteGasStation(@PathVariable Long id) {
        if (!gasStationRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Station not found");
        }
        gasStationRepository.deleteById(id);
        return ResponseEntity.ok("Station deleted");
    }
    @GetMapping("/stations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<GasStation>> getAllStations() {
        List<GasStation> stations = gasStationRepository.findAll();
        return ResponseEntity.ok(stations);
    }
}