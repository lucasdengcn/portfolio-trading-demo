package com.example.demo.market.pricing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RandomPricingTests {

    @Autowired
    RandomPricing randomPricing;

    @Test
    void price() {
        double price = randomPricing.price();
        Assertions.assertTrue(price < 100);
        System.out.println(price);
    }

}