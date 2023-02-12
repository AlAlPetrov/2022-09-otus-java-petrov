package ru.otus.protobuf;


import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.SequentialNumbersService;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(new SequentialNumbersService()).build();
        server.start();
        System.out.println("server waiting for client connections...");

        Runtime.getRuntime().addShutdownHook( new Thread( () -> {
            server.shutdown();
        }));

        server.awaitTermination();
    }
}
