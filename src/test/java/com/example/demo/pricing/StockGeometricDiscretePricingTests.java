/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.pricing;

import com.example.demo.model.Stock;
import com.example.demo.pricing.stock.StockGeometricDiscretePricing;
import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StockGeometricDiscretePricingTests {

    @Test
    void random_price_success() {
        Random random = new Random();
        long ms = 500 + random.nextInt(1500);
        Stock stock = Stock.builder()
                .price(10.0f)
                .deviation(0.1f)
                .expectedReturn(0.2f)
                .build();
        StockGeometricDiscretePricing pricing = new StockGeometricDiscretePricing();
        double price = pricing.price(stock, Duration.ofMillis(ms));
        System.out.println("ms: " + ms + ", price: " + price);
    }

    @Test
    void random_10_price() {
        for (int i = 0; i < 100; i++) {
            random_price_success();
        }
    }

    @Test
    void random_with_zero_time_should_be_current() {
        long ms = 0;
        Stock stock = Stock.builder()
                .price(10.0f)
                .deviation(0.1f)
                .expectedReturn(0.2f)
                .build();
        StockGeometricDiscretePricing pricing = new StockGeometricDiscretePricing();
        double price = pricing.price(stock, Duration.ofMillis(ms));
        Assertions.assertEquals(stock.getPrice(), price);
    }

    @Test
    void generate_with_null_arguments() {
        Stock stock = null;
        StockGeometricDiscretePricing pricing = new StockGeometricDiscretePricing();
        Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                pricing.price(stock, null);
            }
        });
    }
}
