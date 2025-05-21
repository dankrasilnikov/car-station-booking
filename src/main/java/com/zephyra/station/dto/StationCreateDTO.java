package com.zephyra.station.dto;

import java.util.List;

public class StationCreateDTO {
    private String name;
    private int connectorCount;
    private double laitude;
    private double longitude;

    // Геттеры/сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConnectorCount() {
        return connectorCount;
    }

    public void setConnectorCount(int connectorCount) {
        this.connectorCount = connectorCount;
    }

    public double getLaitude() {
        return laitude;
    }

    public void setLaitude(double laitude) {
        this.laitude = laitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

