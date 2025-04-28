package com.example.station.repository;

import com.example.station.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.slot.id = :slotId AND r.status = 'ACTIVE' " +
            "AND (r.startTime < :endTime AND r.endTime > :startTime)")
    List<Reservation> findConflictingReservations(Long slotId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.slot.id = :slotId AND r.status = 'ACTIVE' " +
            "AND r.startTime BETWEEN :startOfDay AND :endOfDay")
    List<Reservation> findReservationsForSlotOnDay(Long slotId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
