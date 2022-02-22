package com.ealanta.grpc.greeting.client;

import com.proto.dummy.DummyMessage;
import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.GreetServiceGrpc.GreetServiceStub;
import com.proto.greet.Greeting;
import com.proto.greet.GreetingOrBuilder;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

  public static void main(String[] args) {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() //de-activates SSL (old?)
        .build();

    //DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);

    //DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);

    //do something
    //syncClient.

    GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
    Greeting greeting = Greeting.newBuilder().setFirstName("Mickey").setLastName("Mouse").build();
    GreetRequest greetingRequest = GreetRequest.newBuilder().setGreeting(greeting).build();
    GreetResponse response = greetClient.greet(greetingRequest);
    System.out.printf("RESPONSE [%s]%n",response.getResult());

    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
