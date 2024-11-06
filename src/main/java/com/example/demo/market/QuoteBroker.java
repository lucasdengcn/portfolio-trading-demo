package com.example.demo.market;

import com.example.demo.market.model.Quote;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * simulate event-driven broker
 */
@Component
public class QuoteBroker {

    private final ConcurrentLinkedQueue<Quote> quotes = new ConcurrentLinkedQueue<>();
    private final List<QuoteConsumer> consumerList = new ArrayList<>();

    public void start(){}

    public void publish(Quote quote){
        quotes.add(quote);
    }

    public void subscribe(QuoteConsumer consumer){
        consumerList.add(consumer);
    }


}
