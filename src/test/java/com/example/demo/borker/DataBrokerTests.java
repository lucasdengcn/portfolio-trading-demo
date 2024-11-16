/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.borker;

import com.example.demo.broker.DataBroker;
import com.example.demo.model.Ticker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DataBrokerTests {

    @Autowired
    DataBroker broker;

    @BeforeEach
    void setup() {
        broker.clear();
    }

    @Test
    void test_on_empty_queue_return_zero() {
        int result = broker.dispatchMessage();
        Assertions.assertEquals(0, result);
    }

    @Test
    void test_publish_ticker_successfully() {
        //
        Ticker quote = Ticker.builder().symbol("A").price(1.0f).build();
        boolean result = broker.publish(quote);
        Assertions.assertTrue(result);
    }

    @Test
    void test_on_non_empty_queue() {
        //
        Ticker quote = Ticker.builder().symbol("A").price(1.0f).build();
        broker.publish(quote);
        //
        int result = broker.dispatchMessage();
        Assertions.assertEquals(1, result);
    }

    @Test
    void test_on_stock_price_queue() {
        //
        Ticker quote = Ticker.builder().symbol("AAPL").price(1.0f).build();
        broker.publish(quote);
        //
        int result = broker.dispatchMessage();
        Assertions.assertEquals(1, result);
    }

    @Test
    void test_publish_null_message() {
        boolean published = broker.publish(null);
        Assertions.assertFalse(published);
    }

    @Test
    void test_publish_empty_message() {
        boolean published = broker.publish(Ticker.builder().build());
        Assertions.assertFalse(published);
    }
}
