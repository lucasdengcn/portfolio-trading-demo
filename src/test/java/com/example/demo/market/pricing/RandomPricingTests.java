/* (C) 2024 */ 

package com.example.demo.market.pricing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RandomPricingTests {

    @Autowired
    RandomPricing randomPricing;

    @Test
    void price() {

    }
}
