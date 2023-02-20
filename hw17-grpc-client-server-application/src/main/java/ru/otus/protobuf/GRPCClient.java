package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import ru.otus.client.ClientStreamObserver;
import ru.otus.protobuf.generated.GetNumbersInRangeRequest;
import ru.otus.protobuf.generated.NumbersServiceGrpc;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final long FIRST_VALUE = 0;
    private static final long LAST_VALUE = 30;
    private static final long MAX_COUNTER = 50;
    private long value;

    public static void main(String[] args) throws InterruptedException {
        var managedChannel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var asyncClient = NumbersServiceGrpc.newStub(managedChannel);
        new GRPCClient().clientAction(asyncClient);

        managedChannel.shutdown();
    }

    private void clientAction(NumbersServiceGrpc.NumbersServiceStub asyncClient) {
        var numberRequest = makeNumberRequest();
        var clientStreamObserver = new ClientStreamObserver();
        asyncClient.getNextNumber(numberRequest, clientStreamObserver);

        for (var counter = 0; counter < MAX_COUNTER; counter++)
        {
            var nextValue = calculateNextValue(clientStreamObserver);
            System.out.println("Current value: "+ nextValue);
            sleep();
        };
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private long calculateNextValue(ClientStreamObserver clientStreamObserver) {
        value = value + clientStreamObserver.getLastValueAndReset() + 1;
        return value;
    }

    private GetNumbersInRangeRequest makeNumberRequest() {
        return GetNumbersInRangeRequest.newBuilder()
                .setFirstValue(FIRST_VALUE)
                .setLastValue(LAST_VALUE)
                .build();
    }
}
