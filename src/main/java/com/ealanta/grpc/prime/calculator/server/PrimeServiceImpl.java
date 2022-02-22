package com.ealanta.grpc.prime.calculator.server;

//a generated file

import com.ealanta.grpc.prime.calculator.PrimeFactorCalculator;
import com.proto.prime.PrimeFactorsRequest;
import com.proto.prime.PrimeFactorsResponse;
import com.proto.prime.PrimeServiceGrpc.PrimeServiceImplBase;
import io.grpc.stub.StreamObserver;

public class PrimeServiceImpl extends PrimeServiceImplBase {


  @Override
  public void primeFactors(PrimeFactorsRequest request,
      StreamObserver<PrimeFactorsResponse> responseObserver) {

    int n = request.getInput();

    int[] factors = PrimeFactorCalculator.primeFactors(n);

    for(int i=0;i<factors.length;i++){
      PrimeFactorsResponse response = PrimeFactorsResponse.newBuilder()
          .setFactor(factors[i])
          .build();
      responseObserver.onNext(response);
    }
    responseObserver.onCompleted();
  }

}
