package com.ealanta.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.SquareRootRequest;
import com.proto.calculator.SquareRootResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class CalculatorSquareRootClient {

  public static void main(String[] args) {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
        .usePlaintext() //de-activates SSL (old?)
        .build();
    int input = -1;
    CalculatorServiceGrpc.CalculatorServiceBlockingStub calcClient = CalculatorServiceGrpc.newBlockingStub(channel);
    SquareRootRequest rootRequest = SquareRootRequest.newBuilder().setInput(input).build();
    try {
      SquareRootResponse response = calcClient.squareRoot(rootRequest);
      System.out.printf("root of [%d] is [%.4f]",input,response.getSquareRoot());
    }catch(StatusRuntimeException ex){
      System.out.printf("code[%s]%n",ex.getStatus().getCode());
      System.out.printf("desc[%s]%n",ex.getStatus().getDescription());
    }

    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
