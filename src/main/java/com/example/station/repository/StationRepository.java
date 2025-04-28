package com.example.station.repository;

import com.example.station.models.ChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<ChargingStation, Long> {
}
