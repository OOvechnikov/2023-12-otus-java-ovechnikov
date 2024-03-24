package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import ru.otus.model.Measurement;

import java.util.List;

@RequiredArgsConstructor
public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public List<Measurement> load() {
        val resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
        return objectMapper.readValue(resourceAsStream, new TypeReference<>() {});
    }

}
