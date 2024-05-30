package ru.otus.api.model;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

public record SensorData(LocalDateTime measurementTime, String room, Double value) implements Comparable<SensorData> {

    @Override
    public String toString() {
        return "SensorData{" + "measurementTime="
                + measurementTime + ", room='"
                + room + '\'' + ", value="
                + value + '}';
    }

    @Override
    public int compareTo(SensorData o) {
        return ChronoLocalDateTime.timeLineOrder().compare(this.measurementTime(), o.measurementTime());
    }

}
