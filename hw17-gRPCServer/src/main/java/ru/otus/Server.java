package ru.otus;

import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.grpc.RemoteServiceImpl;

import java.io.IOException;

@Slf4j
public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {
        val server = ServerBuilder.forPort(8081).addService(new RemoteServiceImpl()).build();
        server.start();

        log.info("Сервер ожидает подключений.");
        server.awaitTermination();
    }

}
