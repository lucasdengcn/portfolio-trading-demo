/* (C) 2024 */ 

package com.example.demo.market.pricing;

import com.example.demo.market.model.Stock;
import java.time.Duration;
import java.util.Random;

/**
 * Discrete Time Geometric Brownian motion for stock prices
 */
public class GeometricDiscretePricing implements StockPricing {

    Random random = new Random();

    public double price(Stock stock, Duration duration) {
        double expectedReturn = stock.getExpectedReturn();
        double deviation = stock.getDeviation();
        //
        double randomFactor = random.nextGaussian();
        //
        double dt = duration.toMillis() * 1.0 / 7257600 / 1000;
        double mu = expectedReturn * dt;
        double sqrt = Math.sqrt(dt);
        double drift = deviation * randomFactor * sqrt;
        //
        double delta = 1 + mu + drift;
        //
        return stock.getPrice() * delta;
    }
}
