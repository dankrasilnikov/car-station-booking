package com.example.station.repository;

import com.example.station.models.ChargingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SlotRepository extends JpaRepository<ChargingSlot, Long> {
    List<ChargingSlot> findByStationId(Long stationId);
}
