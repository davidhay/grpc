package com.ealanta.grpc.prime.calculator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class PrimeFactorCalculatorTest {


  @Test
  public void testFactors(){
    assertArrayEquals(new int[]{2,2,2,3,5}, PrimeFactorCalculator.primeFactors(120));
    assertArrayEquals(new int[]{2, 5, 109, 521}, PrimeFactorCalculator.primeFactors(567890));
  }
}
