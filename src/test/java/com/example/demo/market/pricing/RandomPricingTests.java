/* (C) 2024 */ 

package com.example.demo.market.pricing;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.market.model.Stock;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RandomPricingTests {

    @Test
    void price() {
        RandomPricing randomPricing = new RandomPricing();
        double price = randomPricing.price(Stock.newBuilder().setPrice(100.0).build(), Duration.ofSeconds(1));
        System.out.println(price);
    }
}
