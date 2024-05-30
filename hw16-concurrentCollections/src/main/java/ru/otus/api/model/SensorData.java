package ru.otus.api.model;

import java.time.LocalDateTime;

public record SensorData(LocalDateTime measurementTime, String room, Double value) {

    @Override
    public String toString() {
        return "SensorData{" + "measurementTime="
                + measurementTime + ", room='"
                + room + '\'' + ", value="
                + value + '}';
    }

}
