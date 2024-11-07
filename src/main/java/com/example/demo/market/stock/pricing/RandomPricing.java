/* (C) 2024 */ 

package com.example.demo.market.stock.pricing;

import com.example.demo.market.model.Stock;
import java.time.Duration;
import java.util.Random;

public class RandomPricing implements StockPricing {

    private final Random random = new Random();

    public double price(Stock stock, Duration duration) {
        double v = stock.getPrice() * (1 - random.nextGaussian());
        return v;
    }
}
