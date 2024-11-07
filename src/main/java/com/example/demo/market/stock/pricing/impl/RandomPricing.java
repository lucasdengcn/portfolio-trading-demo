/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.stock.pricing.impl;

import com.example.demo.market.model.Stock;
import com.example.demo.market.stock.pricing.StockPricing;
import java.time.Duration;
import java.util.Random;
import org.springframework.lang.NonNull;

public class RandomPricing implements StockPricing {

    private final Random random = new Random();

    public double price(@NonNull Stock stock, @NonNull Duration duration) {
        double v = stock.getPrice() * (1 - random.nextGaussian());
        return v;
    }
}
