package com.zephyra.station.repository;

import com.zephyra.station.models.Connector;
import com.zephyra.station.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByTitle(String title);
}
