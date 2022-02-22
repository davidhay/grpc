package com.ealanta.grpc.prime.calculator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class PrimeFactorCalculatorTest {


  @Test
  public void testFactors(){
      assertArrayEquals(new int[]{2,2,2,3,5}, PrimeFactorCalculator.primeFactors(120));
  }
}
