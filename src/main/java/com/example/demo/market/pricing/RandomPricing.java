/* (C) 2024 */ 

package com.example.demo.market.pricing;

import com.example.demo.market.model.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Random;

public class RandomPricing implements StockPricing {

    private final Random random = new Random();

    public double price(Stock stock, Duration duration) {
        double v = 100 * random.nextDouble();
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.FLOOR).doubleValue();
    }
}
