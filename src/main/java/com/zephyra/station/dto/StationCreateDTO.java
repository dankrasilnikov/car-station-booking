package com.zephyra.station.dto;

import java.util.List;

public class StationCreateDTO {
    private String name;
    private int connectorCount;

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
}

