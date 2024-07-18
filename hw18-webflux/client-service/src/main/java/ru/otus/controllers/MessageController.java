package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.domain.Message;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private static final String _1408 = "1408";
    private static final String TOPIC_TEMPLATE = "/topic/response.";

    private final WebClient datastoreClient;
    private final SimpMessagingTemplate template;

    @MessageMapping("/message.{roomId}")
    public void getMessage(@DestinationVariable String roomId, Message message) {
        if (_1408.equals(roomId)) {
            log.error("Can't save messages from room {}.", roomId);
            return;
        }
        log.info("get message:{}, roomId:{}", message, roomId);
        saveMessage(roomId, message).subscribe(msgId -> log.info("message send id:{}", msgId));

        convertAndSend(roomId, message);
        convertAndSend(_1408, message);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        val genericMessage = (GenericMessage<byte[]>) event.getMessage();
        val simpDestination = (String) genericMessage.getHeaders().get("simpDestination");
        if (simpDestination == null) {
            log.error("Can not get simpDestination header, headers:{}", genericMessage.getHeaders());
            throw new ChatException("Can not get simpDestination header");
        }
        if (!simpDestination.startsWith(template.getUserDestinationPrefix())) {
            return;
        }
        val roomId = parseRoomId(simpDestination);
        log.info("subscription for: {}, roomId: {}.", simpDestination, roomId);

        val messages = _1408.equals(roomId)
                ? getMessagesAll()
                : getMessagesByRoomId(roomId);

        messages
                .doOnError(ex -> log.error("getting messages for roomId: {} failed", roomId, ex))
                .subscribe(message -> template.convertAndSend(simpDestination, message));
    }

    private void convertAndSend(String roomId, Message message) {
        template.convertAndSend(
                "%s%s".formatted(TOPIC_TEMPLATE, roomId), new Message(HtmlUtils.htmlEscape(message.messageStr()))
        );
    }

    private String parseRoomId(String simpDestination) {
        try {
            val idxRoom = simpDestination.lastIndexOf(TOPIC_TEMPLATE);
            return simpDestination.substring(idxRoom).replace(TOPIC_TEMPLATE, "");
        } catch (Exception ex) {
            log.error("Can't get roomId", ex);
            throw new ChatException("Can not get roomId");
        }
    }

    private Mono<Long> saveMessage(String roomId, Message message) {
        return datastoreClient
                .post()
                .uri("/msg/%s".formatted(roomId))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchangeToMono(response -> response.bodyToMono(Long.class));
    }

    private Flux<Message> getMessagesByRoomId(String roomId) {
        return datastoreClient
                .get()
                .uri("/msg/%s".formatted(roomId))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(this::responseMessage);
    }

    private Flux<Message> getMessagesAll() {
        return datastoreClient
                .get()
                .uri("/msg")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(this::responseMessage);
    }

    private Flux<Message> responseMessage(ClientResponse response) {
        if (response.statusCode().equals(HttpStatus.OK)) {
            return response.bodyToFlux(Message.class);
        }
        return response.createException().flatMapMany(Mono::error);
    }

}
