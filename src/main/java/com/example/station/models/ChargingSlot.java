package com.example.station.models;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChargingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int number;
    @ManyToOne
    @JoinColumn(name = "station_id")
    private ChargingStation station;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
}
