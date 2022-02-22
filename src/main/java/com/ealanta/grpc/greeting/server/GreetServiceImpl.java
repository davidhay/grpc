package com.ealanta.grpc.greeting.server;

//a generated file

import com.proto.greet.GreetManyTimesRequest;
import com.proto.greet.GreetManyTimesResponse;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
import com.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceImplBase {

  @Override
  public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {

    Greeting greeting = request.getGreeting();
    String first = greeting.getFirstName();
    //String last = greeting.getLastName();

    String result = String.format("Hello %s", first);

    GreetResponse response = GreetResponse.newBuilder()
        .setResult(result)
        .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void greetManyTimes(GreetManyTimesRequest request,
      StreamObserver<GreetManyTimesResponse> responseObserver) {

    Greeting greeting = request.getGreeting();
    String first = greeting.getFirstName();
    //String last = greeting.getLastName();

    try {
      for (int i = 0; i < 10; i++) {
        String result = String.format("Hello [%s], response number[%d]", first, i);
        GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
            .setResult(result)
            .build();
        responseObserver.onNext(response);
        Thread.sleep(1000);
      }
    } catch (InterruptedException ex) {
      ex.printStackTrace(System.err);
    } finally {
      responseObserver.onCompleted();
    }
  }
}
