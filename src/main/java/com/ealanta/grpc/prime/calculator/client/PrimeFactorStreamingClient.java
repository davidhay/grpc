package com.ealanta.grpc.prime.calculator.client;

import com.proto.prime.PrimeFactorsRequest;
import com.proto.prime.PrimeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PrimeFactorStreamingClient {

  public static void main(String[] args) {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50053)
        .usePlaintext() //de-activates SSL (old?)
        .build();

    PrimeServiceGrpc.PrimeServiceBlockingStub primeClient = PrimeServiceGrpc.newBlockingStub(channel);
    PrimeFactorsRequest req = PrimeFactorsRequest.newBuilder().setInput(120).build();


    primeClient.primeFactors(req).forEachRemaining( primeFactorsResponse -> {
      System.out.printf("FACTOR [%s]%n",primeFactorsResponse.getFactor());
    });

    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
