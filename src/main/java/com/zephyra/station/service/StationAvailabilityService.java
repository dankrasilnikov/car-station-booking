package com.zephyra.station.service;

import com.zephyra.station.models.GasStation;
import com.zephyra.station.models.StationSlot;
import com.zephyra.station.repository.GasStationRepository;
import com.zephyra.station.repository.StationSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class StationAvailabilityService {

    @Autowired
    GasStationRepository gasStationRepository;
    @Autowired
    StationSlotRepository stationSlotRepository;
    public List<GasStation> getAvailableStations(LocalDateTime timestamp) {
        LocalDateTime slotTime = timestamp.withMinute(0).withSecond(0).withNano(0);
        return gasStationRepository.findAll().stream()
                .filter(station -> {
                    Optional<StationSlot> slotOpt = stationSlotRepository.findByGasStationAndSlotTime(station, slotTime);
                    return slotOpt.isPresent() && slotOpt.get().getAvailableSlots() > 0;
                })
                .collect(Collectors.toList());
    }
}
