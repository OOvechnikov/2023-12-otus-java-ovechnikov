package ru.otus.services;

import lombok.RequiredArgsConstructor;
import ru.otus.api.SensorsDataChannel;
import ru.otus.api.SensorsDataServer;
import ru.otus.api.model.SensorData;

@RequiredArgsConstructor
public class SensorsDataServerImpl implements SensorsDataServer {

    private final SensorsDataChannel sensorsDataChannel;

    @Override
    public void onReceive(SensorData sensorData) {
        sensorsDataChannel.push(sensorData);
    }

}
