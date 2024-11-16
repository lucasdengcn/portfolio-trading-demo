/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.ticker;

import com.example.demo.broker.DataBroker;
import com.example.demo.model.Ticker;
import com.google.common.collect.Sets;
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
        Ticker ticker = dataBroker.peek();
        Assertions.assertNotNull(ticker);
    }

    @Test
    void test_on_publish_new_price_and_consume() {
        tickerProducer.publishNewPrice("A", 12.0f);
        Ticker ticker = dataBroker.peek();
        Assertions.assertNotNull(ticker);
        //
        dataBroker.dispatchMessage();
        //
        ticker = dataBroker.peek();
        //
        Assertions.assertNull(ticker);
    }

    @Test
    void test_generate_price_on_symbols() {
        Set<String> symbols = Sets.newHashSet("AAPL");
        tickerProducer.publishNewPricesInTime(symbols, 1000);
        //
        Ticker ticker = dataBroker.peek();
        Assertions.assertNotNull(ticker);
        //
        dataBroker.dispatchMessage();
        //
        ticker = dataBroker.peek();
        //
        Assertions.assertNull(ticker);
    }

    @Test
    void test_generate_price_on_empty_symbols() {
        Set<String> symbols = Sets.newHashSet();
        tickerProducer.publishNewPricesInTime(symbols, 1000);
    }
}
