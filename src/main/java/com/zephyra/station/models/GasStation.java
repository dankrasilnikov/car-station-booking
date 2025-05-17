package com.zephyra.station.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gas_stations")
public class GasStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stationName;

    public GasStation() {}

    public GasStation(String stationName) {
        this.stationName = stationName;
    }

    public Long getId() {
        return id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
