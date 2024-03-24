package ru.otus.processor.homework;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Message;

import java.time.Clock;
import java.time.Instant;

@ExtendWith(MockitoExtension.class)
@Slf4j
class EvenSecondProcessorTest {

    @Mock
    Clock clock;

    @Test
    @DisplayName("Тестируем EvenSecondProcessor.")
    void evenSecondProcessorTest() {
        var message = new Message.Builder(1L).build();
        try {
            new EvenSecondProcessor(Clock.systemDefaultZone()).process(message);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        var testedProcessor = new EvenSecondProcessor(clock);

        BDDMockito.when(clock.instant()).thenReturn(Instant.parse("2024-03-21T23:54:16.56Z"));
        BDDAssertions.thenThrownBy(() -> testedProcessor.process(message)).hasMessage("Четная секунда.");

        BDDMockito.when(clock.instant()).thenReturn(Instant.parse("2024-03-21T23:54:15.56Z"));
        BDDAssertions.assertThat(testedProcessor.process(message)).isEqualTo(message);
    }

}