package com.ealanta.grpc.greeting.client;

import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.GreetServiceGrpc.GreetServiceBlockingStub;
import com.proto.greet.GreetWithDeadlineRequest;
import com.proto.greet.GreetWithDeadlineResponse;
import com.proto.greet.Greeting;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;

public class GreetingDeadlineClient {

  public static void main(String[] args) {
    System.out.println("Hello I'm a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() //de-activates SSL (old?)
        .build();

    GreetServiceBlockingStub blockingStub = GreetServiceGrpc.newBlockingStub(
            channel);

    int DEADLINE_OKAY = 1000;
    int DEADLINE_TOO_SHORT = 300;
    GreetServiceBlockingStub client = blockingStub.withDeadline(
        Deadline.after(DEADLINE_TOO_SHORT, TimeUnit.MILLISECONDS));
    Greeting greeting = Greeting.newBuilder().setFirstName("Mickey").setLastName("Mouse").build();
    GreetWithDeadlineRequest greetingRequest = GreetWithDeadlineRequest.newBuilder().setGreeting(greeting).build();
    try {
      GreetWithDeadlineResponse response = client.greetWithDeadline(greetingRequest);
      System.out.printf("RESPONSE [%s]%n", response.getResult());
    }catch(StatusRuntimeException ex){
      if(ex.getStatus().getCode() == Status.DEADLINE_EXCEEDED.getCode()){
          System.out.println("REQUEST TIMED OUT");
      }else
        ex.printStackTrace();
    }
    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
