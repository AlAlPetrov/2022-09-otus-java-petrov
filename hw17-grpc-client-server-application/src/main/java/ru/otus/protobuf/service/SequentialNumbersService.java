package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.GetNumbersInRangeRequest;
import ru.otus.protobuf.generated.GetNumbersInRangeResponse;
import ru.otus.protobuf.generated.NumbersServiceGrpc;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SequentialNumbersService extends NumbersServiceGrpc.NumbersServiceImplBase {

    @Override
    public void getNextNumber(GetNumbersInRangeRequest request,
                              StreamObserver<GetNumbersInRangeResponse> responseObserver) {
        var currentValue = new AtomicLong(request.getFirstValue());

        var executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            var nextNumber = currentValue.incrementAndGet();
            var response = GetNumbersInRangeResponse.newBuilder()
                    .setNumber(nextNumber)
                    .build();
            responseObserver.onNext(response);
            if (nextNumber == request.getLastValue()) {
                executor.shutdown();
                responseObserver.onCompleted ();
            }
        };

        executor.scheduleAtFixedRate(task,
                0,
                2,
                TimeUnit.SECONDS);
    }
}
