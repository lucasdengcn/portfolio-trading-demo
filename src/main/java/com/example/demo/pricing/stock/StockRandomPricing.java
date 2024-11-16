/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.pricing.stock;

import com.example.demo.model.Stock;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Random;
import org.springframework.lang.NonNull;

public class StockRandomPricing implements StockPricing {

    private final Random random = new Random();

    public double price(@NonNull Stock stock, @NonNull Duration duration) {
        double v = stock.getPrice() * (1 - random.nextGaussian());
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
