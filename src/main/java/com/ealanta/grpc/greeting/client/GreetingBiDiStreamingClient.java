package com.ealanta.grpc.greeting.client;

import com.proto.greet.GreetEveryoneRequest;
import com.proto.greet.GreetEveryoneResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingBiDiStreamingClient {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() //de-activates SSL (old?)
        .build();

    GreetServiceGrpc.GreetServiceStub greetClient = GreetServiceGrpc.newStub(channel);

    int MESSAGES_TO_SEND = 10;

    final CountDownLatch latch = new CountDownLatch(1);

    StreamObserver<GreetEveryoneRequest> requestObserver = greetClient.greetEveryone(
        new StreamObserver<GreetEveryoneResponse>() {
          @Override
          public void onNext(GreetEveryoneResponse value) {
            System.out.printf("response [%s]%n",value.getResult());
          }

          @Override
          public void onError(Throwable t) {
              t.printStackTrace();
          }

          @Override
          public void onCompleted() {
            System.out.println("fin");
            latch.countDown();
          }
        });

    for(int i=0;i<MESSAGES_TO_SEND;i++){
      Greeting greeting = Greeting.newBuilder().setFirstName("Mickey"+i).setLastName("Mouse").build();
      requestObserver.onNext(GreetEveryoneRequest.newBuilder().setGreeting(greeting).build());
    }
    requestObserver.onCompleted();

    //how do I wait until done?
    latch.await(3, TimeUnit.SECONDS);
    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
