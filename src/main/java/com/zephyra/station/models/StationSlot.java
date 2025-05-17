package com.zephyra.station.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "station_slots")
public class StationSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(
            name = "gas_station_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (gas_station_id) REFERENCES gas_stations(id) ON DELETE CASCADE"
            )
    )
    private GasStation gasStation;

    private LocalDateTime slotTime; // начало часа (например, 2025-05-08 11:00)

    private int availableSlots; // сколько слотов осталось на этот час

    // Конструкторы
    public StationSlot() {}

    public StationSlot(GasStation gasStation, LocalDateTime slotTime, int availableSlots) {
        this.gasStation = gasStation;
        this.slotTime = slotTime;
        this.availableSlots = availableSlots;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public GasStation getGasStation() {
        return gasStation;
    }

    public LocalDateTime getSlotTime() {
        return slotTime;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGasStation(GasStation gasStation) {
        this.gasStation = gasStation;
    }

    public void setSlotTime(LocalDateTime slotTime) {
        this.slotTime = slotTime;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }
}