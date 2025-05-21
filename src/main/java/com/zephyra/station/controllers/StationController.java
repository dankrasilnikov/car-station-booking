package com.zephyra.station.controllers;

import com.vladmihalcea.hibernate.type.range.Range;
import com.zephyra.station.dto.ConnectorDTO;
import com.zephyra.station.errors.BookingInThePastException;
import com.zephyra.station.models.Station;
import com.zephyra.station.repository.ConnectorRepository;

import com.zephyra.station.service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    @Autowired
    ConnectorRepository connectorRepo;

    /** GET http://localhost:8080/api/stations/2/free?timestamp=1747731600 */
    @GetMapping("/{id}/free")
    public List<ConnectorDTO> freeAt(@PathVariable Long id,
                                     @RequestParam Long timestamp) {
        ZonedDateTime start = Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.UTC);
        ZonedDateTime end = start.plusSeconds(1);
        if (start.isBefore(ZonedDateTime.now(ZoneOffset.UTC))) {
            throw new BookingInThePastException("Reservation time cannot be in the past");
        }
        return connectorRepo.findFreeAt(id, start, end).stream()
                .map(ConnectorDTO::from)
                .toList();
    }

}
