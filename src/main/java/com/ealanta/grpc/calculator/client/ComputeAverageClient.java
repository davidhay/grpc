package com.ealanta.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.ComputeAverageRequest;
import com.proto.calculator.ComputeAverageResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class ComputeAverageClient {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
        .usePlaintext() //de-activates SSL (old?)
        .build();

    CalculatorServiceGrpc.CalculatorServiceStub calcClient = CalculatorServiceGrpc.newStub(channel);

    CountDownLatch latch = new CountDownLatch(1);

    int[] values = {1,2,3,4};

    StreamObserver<ComputeAverageRequest> requestObserver = calcClient.computeAverage(
        new StreamObserver<ComputeAverageResponse>() {
          @Override
          public void onNext(ComputeAverageResponse value) {
            System.out.printf("average of %s is %.4f%n", Arrays.toString(values), value.getAverage());
          }

          @Override
          public void onError(Throwable t) {
            t.printStackTrace(System.err);
          }

          @Override
          public void onCompleted() {
            latch.countDown();
            System.out.println("FIN");
          }
        });
    for(int i=0;i< values.length;i++){
      requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(values[i]).build());
    }
    requestObserver.onCompleted();
    latch.await();

    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
