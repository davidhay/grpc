package com.ealanta.grpc.calculator.client;

import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.CalculatorServiceGrpc.CalculatorServiceStub;
import com.proto.calculator.FindMaximumRequest;
import com.proto.calculator.FindMaximumResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FindSteamingMaxClient {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
        .usePlaintext() //de-activates SSL (old?)
        .build();

    CalculatorServiceStub calcClient = CalculatorServiceGrpc.newStub(channel);

    CountDownLatch latch = new CountDownLatch(1);

    StreamObserver<FindMaximumRequest> streamObserverReq = calcClient.findMaximum(
        new StreamObserver<FindMaximumResponse>() {
          @Override
          public void onNext(FindMaximumResponse value) {
            System.out.printf("current max[%d]%n", value.getCurrentMaximum());
          }

          @Override
          public void onError(Throwable t) {
            t.printStackTrace();
          }

          @Override
          public void onCompleted() {
            latch.countDown();
          }
        });

    int values[] = {1, 5, 3, 6, 2, 20};
    Arrays.stream(values).forEach(val -> {
      FindMaximumRequest request = FindMaximumRequest.newBuilder().setNumber(val).build();
      streamObserverReq.onNext(request);
    });

    latch.await(3, TimeUnit.SECONDS);
    System.out.println("Shutting down channel");
    channel.shutdown();
  }

}
