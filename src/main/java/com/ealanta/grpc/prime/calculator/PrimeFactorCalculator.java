package com.ealanta.grpc.prime.calculator;

import java.util.ArrayList;
import java.util.List;

public class PrimeFactorCalculator {

  public static int[] primeFactors(final int input) {
    List<Integer> temp = new ArrayList<>();

    int k = 2;
    int n = input;
    while (n > 1) {
      // if k evenly divides into N
      if (n % k == 0) {
        temp.add(k);      // this is a factor
        n = n / k;    // divide N by k so that we have the rest of the number left.
      } else {
        k = k + 1;
      }
    }
    int[] result = temp.stream().mapToInt(i->i).toArray();
    return result;
  }
}
