/* (C) 2024 */ 

package com.example.demo.market;

import com.example.demo.market.model.Quote;
import com.example.demo.market.model.QuoteBatch;
import com.example.demo.market.producer.QuoteBroker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuoteBrokerTests {

    @Autowired
    QuoteBroker broker;

    @BeforeEach
    void setup() {
        broker.clear();
    }

    @Test
    void test_on_empty_queue() {
        int result = broker.dispatchMessage();
        Assertions.assertEquals(0, result);
    }

    @Test
    void test_on_non_empty_queue() {
        //
        Quote quote = Quote.newBuilder().setSymbol("A").setPrice(1.0f).build();
        broker.publish(QuoteBatch.newBuilder().addItems(quote).build());
        //
        int result = broker.dispatchMessage();
        Assertions.assertEquals(1, result);
    }

    @Test
    void test_on_stock_price_queue() {
        //
        Quote quote = Quote.newBuilder().setSymbol("AAPL").setPrice(1.0f).build();
        broker.publish(QuoteBatch.newBuilder().addItems(quote).build());
        //
        int result = broker.dispatchMessage();
        Assertions.assertEquals(1, result);
    }
}
