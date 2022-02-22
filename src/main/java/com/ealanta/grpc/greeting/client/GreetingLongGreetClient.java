package com.ealanta.grpc.greeting.client;

import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import com.proto.greet.LongGreetRequest;
import com.proto.greet.LongGreetResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingLongGreetClient {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() //de-activates SSL (old?)
        .build();

    GreetServiceGrpc.GreetServiceStub greetClient = GreetServiceGrpc.newStub(channel);

    final CountDownLatch latch = new CountDownLatch(1);

    StreamObserver<LongGreetRequest> requestObserver = greetClient.longGreet(
        new StreamObserver<LongGreetResponse>() {
          @Override
          public void onNext(LongGreetResponse value) {
            System.out.printf("FROM SERVER %s%n", value.getResult());
          }

          @Override
          public void onError(Throwable t) {
            t.printStackTrace(System.err);
          }

          @Override
          public void onCompleted() {
            System.out.println("FIN.");
            latch.countDown();
          }
        });

    for(int i=0;i<10;i++){
      Greeting greeting = Greeting.newBuilder().setFirstName("Mickey"+i).setLastName("Mouse").build();
      requestObserver.onNext(LongGreetRequest.newBuilder().setGreeting(greeting).build());
    }
    requestObserver.onCompleted();

    //how do I wait until done?
    latch.await(3, TimeUnit.SECONDS);
    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
