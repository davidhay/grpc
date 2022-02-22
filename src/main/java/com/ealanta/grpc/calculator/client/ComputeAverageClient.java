package com.ealanta.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.ComputeAverageRequest;
import com.proto.calculator.ComputeAverageResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
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


    StreamObserver<ComputeAverageRequest> requestObserver = calcClient.computeAverage(
        new StreamObserver<ComputeAverageResponse>() {
          @Override
          public void onNext(ComputeAverageResponse value) {
            System.out.printf("average of %s is %.4f%n", "0 to 9999", value.getAverage());
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
    Instant now = Instant.now();
    for(int i=0;i<10000;i++){
      requestObserver.onNext(ComputeAverageRequest.newBuilder().setNumber(i).build());
    }
    requestObserver.onCompleted();
    latch.await();
    long diff = Instant.now().toEpochMilli() - now.toEpochMilli();
    System.out.printf("took [%d]ms%n",diff);
    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
