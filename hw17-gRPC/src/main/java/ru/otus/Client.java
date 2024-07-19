package ru.otus;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.protobuf.GrpcServiceGrpc;
import ru.otus.protobuf.ValueMessage;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Client {

    private static AtomicLong serverCurrentValue = new AtomicLong(0);
    private static boolean needToUpdateServerCurrentValue = false;

    @SneakyThrows
    public static void main(String[] args) {

        val newStub = GrpcServiceGrpc
                .newStub(ManagedChannelBuilder.forAddress("localhost", 8081)
                        .usePlaintext()
                        .build());
        newStub.exchange(ValueMessage.newBuilder()
                .setFirstValue(1)
                .setLastValue(30)
                .build(), new StreamObserver<>() {
            @Override
            public void onNext(ValueMessage um) {
                serverCurrentValue.updateAndGet(v -> um.getCurrentValue());
                needToUpdateServerCurrentValue = true;
                log.info("Число от сервера: " + um.getCurrentValue());
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("Конец.");
            }
        });

        long currentValue = 0;
        for (int i = 0; i < 50; i++) {
            if (needToUpdateServerCurrentValue) {
                currentValue = currentValue + serverCurrentValue.getAndSet(0) + 1L;
                needToUpdateServerCurrentValue = false;
            } else {
                currentValue = currentValue + 1L;
            }

            log.info("Текущее значение: {}.", currentValue);
            Thread.sleep(1000);
        }

        ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build().shutdown();
    }
}
