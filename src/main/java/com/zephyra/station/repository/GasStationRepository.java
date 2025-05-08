package com.zephyra.station.repository;

import com.zephyra.station.models.GasStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface GasStationRepository extends JpaRepository<GasStation, Long> {

    @Query("""
        SELECT g 
        FROM GasStation g 
        WHERE EXISTS (
            SELECT 1 
            FROM StationSlot s 
            WHERE s.gasStation = g 
              AND s.slotTime = :timestamp 
              AND s.availableSlots > 0
        )
    """)
    List<GasStation> findAvailableStationsAtTime(@Param("timestamp") LocalDateTime timestamp);
}
