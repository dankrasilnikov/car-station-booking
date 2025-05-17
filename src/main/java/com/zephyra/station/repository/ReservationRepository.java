package com.zephyra.station.repository;

import com.zephyra.station.models.GasStation;
import com.zephyra.station.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByGasStationAndEndTimeAfterAndStartTimeBefore(
            GasStation gasStation, LocalDateTime start, LocalDateTime end
    );
}
