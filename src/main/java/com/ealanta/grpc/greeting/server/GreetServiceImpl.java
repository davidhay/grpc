package com.ealanta.grpc.greeting.server;

//a generated file

import com.proto.greet.GreetEveryoneRequest;
import com.proto.greet.GreetEveryoneResponse;
import com.proto.greet.GreetManyTimesRequest;
import com.proto.greet.GreetManyTimesResponse;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
import com.proto.greet.Greeting;
import com.proto.greet.LongGreetRequest;
import com.proto.greet.LongGreetResponse;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceImplBase {

  @Override
  public StreamObserver<GreetEveryoneRequest> greetEveryone(
      StreamObserver<GreetEveryoneResponse> responseObserver) {

    return new StreamObserver<GreetEveryoneRequest>() {

      @Override
      public void onNext(GreetEveryoneRequest value) {
        Greeting greeting = value.getGreeting();
        String first = greeting.getFirstName();
        String last = greeting.getLastName();

        String result = String.format("Hello %s %s!",first,last);
        GreetEveryoneResponse resp = GreetEveryoneResponse.newBuilder().setResult(result).build();
        responseObserver.onNext(resp);
      }

      @Override
      public void onError(Throwable t) {
        t.printStackTrace(System.err);
        responseObserver.onError(new Exception("exception when reading",t));
      }

      @Override
      public void onCompleted() {
          responseObserver.onCompleted();
      }
    };
  }

  //you return how the handler returns to a stream

  @Override
  public StreamObserver<LongGreetRequest> longGreet(
      StreamObserver<LongGreetResponse> responseObserver) {

    StreamObserver<LongGreetRequest> handler = new StreamObserver<LongGreetRequest>() {

      String buffer = "";

      @Override
      public void onNext(LongGreetRequest value) {
        buffer += String.format("Hello [%s]%n",value.getGreeting().getFirstName());
      }

      @Override
      public void onError(Throwable t) {
        t.printStackTrace(System.err);
        responseObserver.onError(t);
      }

      @Override
      public void onCompleted() {
            LongGreetResponse response = LongGreetResponse.newBuilder().setResult(buffer).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
      }
    };
    return handler;
  }

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
