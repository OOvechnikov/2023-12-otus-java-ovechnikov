package ru.otus.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Message;
import ru.otus.domain.MessageDto;
import ru.otus.service.DataStore;

@RequiredArgsConstructor
@RestController
@Slf4j
public class DataController {

    private final DataStore dataStore;
    private final Scheduler workerPool;

    @PostMapping(value = "/msg/{roomId}")
    public Mono<Long> messageFromChat(@PathVariable("roomId") String roomId, @RequestBody MessageDto messageDto) {
        val messageStr = messageDto.messageStr();

        val msgId = Mono.just(new Message(roomId, messageStr))
                .doOnNext(msg -> log.info("messageFromChat:{}", msg))
                .flatMap(dataStore::saveMessage)
                .publishOn(workerPool)
                .doOnNext(msgSaved -> log.info("msgSaved id:{}", msgSaved.getId()))
                .map(Message::getId)
                .subscribeOn(workerPool);

        log.info("messageFromChat, roomId:{}, msg:{} done", roomId, messageStr);
        return msgId;
    }

    @GetMapping(value = "/msg/{roomId}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MessageDto> getMessagesByRoomId(@PathVariable("roomId") String roomId) {
        return Mono.just(roomId)
                .doOnNext(room -> log.info("getMessagesByRoomId, room:{}", room))
                .flatMapMany(dataStore::loadMessages)
                .map(message -> new MessageDto(message.getMsgText()))
                .doOnNext(msgDto -> log.info("msgDto:{}", msgDto))
                .subscribeOn(workerPool);
    }

    @GetMapping(value = "/msg", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MessageDto> getMessagesByRoomId() {
        log.info("all messages");
        return dataStore.loadMessagesAll()
                .map(message -> new MessageDto(message.getMsgText()))
                .doOnNext(msgDto -> log.info("msgDto:{}", msgDto))
                .subscribeOn(workerPool);
    }

}
