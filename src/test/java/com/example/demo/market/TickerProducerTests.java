/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market;

import com.example.demo.broker.DataBroker;
import com.example.demo.market.ticker.TickerProducer;
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
class TickerProducerTests {

    @Autowired
    TickerProducer tickerProducer;

    @Autowired
    DataBroker dataBroker;

    @Test
    void test_on_publish_new_price() {
        tickerProducer.publishNewPrice("A", 1.0f);
        ByteString byteString = dataBroker.peek();
        Assertions.assertNotNull(byteString);
    }

    @Test
    void test_on_publish_new_price_and_consume() {
        tickerProducer.publishNewPrice("A", 12.0f);
        ByteString byteString = dataBroker.peek();
        Assertions.assertNotNull(byteString);
        //
        dataBroker.dispatchMessage();
        //
        byteString = dataBroker.peek();
        //
        Assertions.assertNull(byteString);
    }

    @Test
    void test_generate_price_on_symbols() {
        Set<String> symbols = Sets.newHashSet("AAPL");
        tickerProducer.generatePublishPrice(symbols, 1000);
        //
        ByteString byteString = dataBroker.peek();
        Assertions.assertNotNull(byteString);
        //
        dataBroker.dispatchMessage();
        //
        byteString = dataBroker.peek();
        //
        Assertions.assertNull(byteString);
    }

    @Test
    void test_generate_price_on_empty_symbols() {
        Set<String> symbols = Sets.newHashSet();
        tickerProducer.generatePublishPrice(symbols, 1000);
    }
}
