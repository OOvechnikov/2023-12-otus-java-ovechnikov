package ru.otus.grpc;

import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.protobuf.ModelMessage;
import ru.otus.protobuf.RemoteServiceGrpc;

@Slf4j
public class RemoteServiceImpl extends RemoteServiceGrpc.RemoteServiceImplBase {

    @SneakyThrows
    public void exchange(ModelMessage request, StreamObserver<ModelMessage> responseObserver) {
        log.info("Range: " + request.getFirstValue() + "..." + request.getLastValue());

        for (long i = request.getFirstValue(); i <= request.getLastValue(); i++) {
            Thread.sleep(2000);
            log.info("Текущее значение сервера: " + i);

            val model = ModelMessage.newBuilder()
                    .setCurrentValue(i)
                    .build();
            responseObserver.onNext(model);
        }

        responseObserver.onCompleted();
    }

}
