package ru.otus.processor.homework;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

@Slf4j
public class SwapFieldsProcessor implements Processor {

    @Override
    public Message process(Message message) {
        log.debug("Меняем местами значения {} и {}.", message.getField11(), message.getField12());
        val field11 = message.getField11();
        return message.toBuilder().field11(message.getField12()).field12(field11).build();
    }

}
