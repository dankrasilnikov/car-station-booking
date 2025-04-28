package com.example.station.models;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class ChargingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String stationName;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    private List<ChargingSlot> slots = new ArrayList<>();
}
