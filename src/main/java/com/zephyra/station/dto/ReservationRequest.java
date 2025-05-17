package com.zephyra.station.dto;

public class ReservationRequest {
    private Long userId;
    private Long gasStationId;

    // Геттеры и сеттеры
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGasStationId() {
        return gasStationId;
    }
    public void setGasStationId(Long gasStationId) {
        this.gasStationId = gasStationId;
    }
}
