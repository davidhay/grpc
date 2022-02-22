package com.ealanta.grpc.calculator.server;

//a generated file
import com.proto.calculator.AdditionRequest;
import com.proto.calculator.AdditionResponse;
import com.proto.calculator.CalculatorServiceGrpc.CalculatorServiceImplBase;
import com.proto.calculator.ComputeAverageRequest;
import com.proto.calculator.ComputeAverageResponse;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceImplBase {

  @Override
  public StreamObserver<ComputeAverageRequest> computeAverage(
      StreamObserver<ComputeAverageResponse> responseObserver) {

      return new StreamObserver<ComputeAverageRequest>() {

        int total = 0;
        int count = 0;

        @Override
        public void onNext(ComputeAverageRequest value) {
          total += value.getNumber();
          count ++;
          System.out.printf("SERVER TOLD TO BUFFER %d%n", value.getNumber());
        }

        @Override
        public void onError(Throwable t) {
          t.printStackTrace(System.err);
          responseObserver.onError(t);

        }

        @Override
        public void onCompleted() {
              System.out.println("SERVER TOLD TO CALC");
              double average = (double)total / count;
              responseObserver.onNext(ComputeAverageResponse.newBuilder().setAverage(average).build());
              responseObserver.onCompleted();
        }
      };
  }

  @Override
  public void add(AdditionRequest request, StreamObserver<AdditionResponse> responseObserver) {

    int arg1 = request.getArg1();
    int arg2 = request.getArg2();

    AdditionResponse resp = AdditionResponse.newBuilder().setResult(arg1+arg2).build();

    responseObserver.onNext(resp);
    responseObserver.onCompleted();
  }

}
