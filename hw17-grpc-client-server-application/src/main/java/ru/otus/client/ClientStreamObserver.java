package ru.otus.client;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.GetNumbersInRangeResponse;

public class ClientStreamObserver implements StreamObserver<GetNumbersInRangeResponse> {
    private long lastValue;

    public synchronized long getLastValueAndReset() {
        var lastValue = this.lastValue;
        this.lastValue = 0;

        return lastValue;
    }

    @Override
    public void onNext(GetNumbersInRangeResponse getNumbersInRangeResponse) {
        setLastValue(getNumbersInRangeResponse.getNumber());
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {

    }

    private synchronized void setLastValue(long value) {
        this.lastValue = value;
    }
}
