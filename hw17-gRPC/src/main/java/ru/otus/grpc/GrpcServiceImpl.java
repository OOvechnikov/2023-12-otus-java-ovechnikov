package ru.otus.grpc;

import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.protobuf.GrpcServiceGrpc;
import ru.otus.protobuf.ValueMessage;

@Slf4j
public class GrpcServiceImpl extends GrpcServiceGrpc.GrpcServiceImplBase {

    @SneakyThrows
    public void exchange(ValueMessage request, StreamObserver<ValueMessage> responseObserver) {
        log.info("Диапазон: " + request.getFirstValue() + "..." + request.getLastValue());

        for (long i = request.getFirstValue(); i <= request.getLastValue(); i++) {
            Thread.sleep(2000);
            log.info("Текущее значение сервера: " + i);

            val model = ValueMessage.newBuilder()
                    .setCurrentValue(i)
                    .build();
            responseObserver.onNext(model);
        }

        responseObserver.onCompleted();
    }

}
