/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.pricing;

import com.example.demo.model.Stock;
import com.example.demo.pricing.stock.StockRandomPricing;
import java.time.Duration;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StockRandomPricingTests {

    @RepeatedTest(10)
    void price() {
        StockRandomPricing randomPricing = new StockRandomPricing();
        Stock stock = Stock.builder().price(100.0).build();
        double price = randomPricing.price(stock, Duration.ofSeconds(1));
    }
}
