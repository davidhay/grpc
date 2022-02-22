package com.ealanta.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class CalculatorServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("Hello gRPC");

    Server server = ServerBuilder.forPort(50052)
        .addService(new CalculatorServiceImpl())
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
