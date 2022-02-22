package com.ealanta.grpc.greeting.server;

//a generated file
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

    String result = String.format("Hello %s",first);

    GreetResponse response = GreetResponse.newBuilder()
        .setResult(result)
        .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
