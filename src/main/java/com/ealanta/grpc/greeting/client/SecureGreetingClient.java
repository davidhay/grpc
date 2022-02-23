package com.ealanta.grpc.greeting.client;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import java.io.File;
import javax.net.ssl.SSLException;

public class SecureGreetingClient {

  public static void main(String[] args) throws SSLException {
    System.out.println("Hello I'm a gRPC client");

    /*
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() //de-activates SSL (old?)
        .build();
     */

    ManagedChannel secureChanel = NettyChannelBuilder.forAddress("localhost", 50051)
        .sslContext(
            GrpcSslContexts.forClient().trustManager(new File("ssl/ca.crt")).build())
        .build();

    GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(secureChanel);
    Greeting greeting = Greeting.newBuilder().setFirstName("Mickey").setLastName("Mouse").build();
    GreetRequest greetingRequest = GreetRequest.newBuilder().setGreeting(greeting).build();
    GreetResponse response = greetClient.greet(greetingRequest);
    System.out.printf("RESPONSE [%s]%n",response.getResult());

    System.out.println("Shutting down channel");
    secureChanel.shutdown();
  }
}
