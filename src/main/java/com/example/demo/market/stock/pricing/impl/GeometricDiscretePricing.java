/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.stock.pricing.impl;

import com.example.demo.market.model.Stock;
import com.example.demo.market.stock.pricing.StockPricing;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Random;
import org.springframework.lang.NonNull;

/**
 * Discrete Time Geometric Brownian motion for stock prices
 */
public class GeometricDiscretePricing implements StockPricing {

    Random random = new Random();

    public double price(@NonNull Stock stock, @NonNull Duration duration) {
        if (null == stock || null == duration) {
            throw new IllegalArgumentException("stock or duration can't be NULL");
        }
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
        double v = stock.getPrice() * delta;
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
