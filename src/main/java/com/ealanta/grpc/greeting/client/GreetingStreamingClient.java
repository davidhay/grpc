package com.ealanta.grpc.greeting.client;

import com.proto.greet.GreetManyTimesRequest;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingStreamingClient {

  public static void main(String[] args) {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() //de-activates SSL (old?)
        .build();

    GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
    Greeting greeting = Greeting.newBuilder().setFirstName("Mickey").setLastName("Mouse").build();
    GreetManyTimesRequest greetingRequest = GreetManyTimesRequest.newBuilder().setGreeting(greeting).build();

    greetClient.greetManyTimes(greetingRequest).forEachRemaining( greetManyTimesResponse -> {
      System.out.printf("RESPONSE [%s]%n",greetManyTimesResponse.getResult());
    });

    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
