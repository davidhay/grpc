package com.ealanta.grpc.greeting.server;

//a generated file
import com.proto.calculator.AdditionRequest;
import com.proto.calculator.AdditionResponse;
import com.proto.calculator.CalculatorServiceGrpc.CalculatorServiceImplBase;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
import com.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceImplBase {

  @Override
  public void add(AdditionRequest request, StreamObserver<AdditionResponse> responseObserver) {

    int arg1 = request.getArg1();
    int arg2 = request.getArg2();

    AdditionResponse resp = AdditionResponse.newBuilder().setResult(arg1+arg2).build();

    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

}
