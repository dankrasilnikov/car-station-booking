package com.zephyra.station.repository;

import com.zephyra.station.models.Station;
import com.zephyra.station.models.Reservation;
import com.zephyra.station.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser(User user);
    @Modifying
    @Query(value = "DELETE FROM reservation WHERE upper(period) < :now", nativeQuery = true)
    void deleteExpired(@Param("now") ZonedDateTime now);
}
