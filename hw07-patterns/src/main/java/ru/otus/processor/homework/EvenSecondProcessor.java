package ru.otus.processor.homework;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalField;

@RequiredArgsConstructor
@Slf4j
public class EvenSecondProcessor implements Processor {

    private final Clock clock;

    @Override
    public Message process(Message message) {
        val instant = clock.instant();
        log.debug("Текущее время: {}", instant.atZone(ZoneId.systemDefault()));
        if (instant.getEpochSecond() % 2 == 0) {
            throw new RuntimeException("Четная секунда.");
        }
        return message;
    }

}
