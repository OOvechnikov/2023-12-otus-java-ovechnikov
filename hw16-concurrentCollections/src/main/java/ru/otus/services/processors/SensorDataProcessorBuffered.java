package ru.otus.services.processors;

import lombok.extern.slf4j.Slf4j;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final NavigableSet<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        dataBuffer = new ConcurrentSkipListSet<>();
    }

    @Override
    public synchronized void process(SensorData data) {
        dataBuffer.add(data);
        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    public synchronized void flush() {
        if (dataBuffer.isEmpty()) {
            return;
        }
        try {
            writer.writeBufferedData(dataBuffer.stream().toList());
            dataBuffer.clear();
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }

}
