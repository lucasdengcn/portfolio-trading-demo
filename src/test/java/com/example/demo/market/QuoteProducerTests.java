/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market;

import com.example.demo.market.producer.QuoteBroker;
import com.example.demo.market.producer.QuoteProducer;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuoteProducerTests {

    @Autowired
    QuoteProducer quoteProducer;

    @Autowired
    QuoteBroker quoteBroker;

    @Test
    void test_on_publish_new_price() {
        quoteProducer.publishNewPrice("A", 1.0f);
        ByteString byteString = quoteBroker.peek();
        Assertions.assertNotNull(byteString);
    }

    @Test
    void test_on_publish_new_price_and_consume() {
        quoteProducer.publishNewPrice("A", 12.0f);
        ByteString byteString = quoteBroker.peek();
        Assertions.assertNotNull(byteString);
        //
        quoteBroker.dispatchMessage();
        //
        byteString = quoteBroker.peek();
        //
        Assertions.assertNull(byteString);
    }

    @Test
    void test_generate_price_on_symbols() {
        Set<String> symbols = Sets.newHashSet("AAPL");
        quoteProducer.generatePublishPrice(symbols, 1000);
        //
        ByteString byteString = quoteBroker.peek();
        Assertions.assertNotNull(byteString);
        //
        quoteBroker.dispatchMessage();
        //
        byteString = quoteBroker.peek();
        //
        Assertions.assertNull(byteString);
    }

    @Test
    void test_generate_price_on_empty_symbols() {
        Set<String> symbols = Sets.newHashSet();
        quoteProducer.generatePublishPrice(symbols, 1000);
    }
}
