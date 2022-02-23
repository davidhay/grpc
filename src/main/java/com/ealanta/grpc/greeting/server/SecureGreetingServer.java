package com.ealanta.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.File;
import java.io.IOException;

public class SecureGreetingServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("Hello SECURE gRPC - port 50051");

    Server server = ServerBuilder.forPort(50051)
        .addService(new GreetServiceImpl())
        .useTransportSecurity(
            new File("ssl/server.crt"),
            new File("ssl/server.pem"))
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
