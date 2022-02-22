package com.ealanta.grpc.greeting.client;

import com.proto.calculator.AdditionRequest;
import com.proto.calculator.AdditionResponse;
import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {

  public static void main(String[] args) {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
        .usePlaintext() //de-activates SSL (old?)
        .build();

    CalculatorServiceGrpc.CalculatorServiceBlockingStub calcClient = CalculatorServiceGrpc.newBlockingStub(channel);
    int arg1 = 1111;
    int arg2 = 2222;
    AdditionRequest addRequest = AdditionRequest.newBuilder().setArg1(arg1).setArg2(arg2).build();
    AdditionResponse response = calcClient.add(addRequest);

    System.out.printf("RESPONSE [%d]+[%d]=[%s]%n",arg1,arg2,response.getResult());

    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
