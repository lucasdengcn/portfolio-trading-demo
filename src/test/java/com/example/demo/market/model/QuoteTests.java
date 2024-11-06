package com.example.demo.market.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class QuoteTests {

    @Test
    void test_create_quote(){
        Quote quote = Quote.newBuilder().setSymbol("A").setPrice(1.2f)
                .build();

        Assertions.assertEquals(1.2f, quote.getPrice());
        Assertions.assertEquals("A", quote.getSymbol());
    }

}
