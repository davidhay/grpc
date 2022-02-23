package com.ealanta.grpc.calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import java.io.IOException;

public class CalculatorServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("Hello gRPC");

    Server server = ServerBuilder.forPort(50052)
        .addService(new CalculatorServiceImpl())
        .addService(ProtoReflectionService.newInstance())
        .build();

    server.start();

    //nice
    Runtime.getRuntime().addShutdownHook( new Thread(() -> {
      System.out.println("Received Shutdown Request");
      server.shutdown();
      System.out.println("Successfully stopped the grpc server");
    }));

    server.awaitTermination();

  }
}
