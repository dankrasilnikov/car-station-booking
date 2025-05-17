package com.zephyra.station.repository;

import com.zephyra.station.models.GasStation;
import com.zephyra.station.models.StationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface StationSlotRepository extends JpaRepository<StationSlot, Long> {
    Optional<StationSlot> findByGasStationAndSlotTime(GasStation station, LocalDateTime slotTime);
}
