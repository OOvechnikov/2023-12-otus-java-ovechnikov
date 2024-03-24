package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Map;

@RequiredArgsConstructor
public class FileSerializer implements Serializer {

    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public void serialize(Map<String, Double> data) {
        objectMapper.writeValue(new File(fileName), data);
    }

}
