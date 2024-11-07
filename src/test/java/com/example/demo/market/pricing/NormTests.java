/* (C) 2024 */ 

package com.example.demo.market.pricing;

import com.example.demo.market.option.pricing.Norm;
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
        System.out.println(probability);
    }
}
