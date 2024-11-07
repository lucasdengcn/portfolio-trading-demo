/* (C) 2024 */ 

package com.example.demo.market.pricing;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.market.model.Stock;
import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GeometricDiscretePricingTests {

    @Test
    void random_price() {
        Random random = new Random();
        long ms = 500 + random.nextInt(1500);
        Stock stock = Stock.newBuilder()
                .setPrice(10.0f)
                .setDeviation(0.1f)
                .setExpectedReturn(0.2f)
                .build();
        GeometricDiscretePricing pricing = new GeometricDiscretePricing();
        double price = pricing.price(stock, Duration.ofMillis(ms));
        System.out.println("ms: " + ms + ", price: " + price);
    }

    @Test
    void random_10_price() {
        for (int i = 0; i < 100; i++) {
            random_price();
        }
    }
}
