/* (C) 2024 */ 

package com.example.demo.market.pricing;

import com.example.demo.market.utils.Norm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NormTests {

    @Test
    void erf() {
        double v = Norm.erf(0.234);
        System.out.println(v);
        Assertions.assertTrue(v > 0 && v < 1.0);
    }

    @Test
    void stock_price_exceed_100_prob() {
        double mean = 95.6;
        double standardDeviation = 2.3;
        double x = (100 - mean) / standardDeviation;
        double probability = Norm.erf(x);
        Assertions.assertTrue(probability > 0);
    }

    @Test
    void cdf() {
        double mean = 95.6;
        double standardDeviation = 2.3;
        double x = (100 - mean) / standardDeviation;
        double probability = Norm.cdf(x);
        Assertions.assertTrue(probability > 0);
    }

    @Test
    void standard_deviation() {
        double std = Norm.standardDeviation(new double[] {1.0, 2.0, 3.0});
        Assertions.assertTrue(std > 0);
    }

    @Test
    void calculate_mean() {
        double std = Norm.mean(new double[] {1.0, 2.0, 3.0});
        Assertions.assertTrue(std > 0);
    }
}
