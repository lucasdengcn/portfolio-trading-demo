package com.example.demo.market;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuoteProducer {

    /**
     * simulate event-driven broker
     */
    private final QuoteBroker broker;
    private final ListedStock listedStock;

    /**
     * broker to store latest quotes
     * @param broker
     */
    public QuoteProducer(QuoteBroker broker, ListedStock listedStock) {
        this.broker = broker;
        this.listedStock = listedStock;
    }

    /**
     * start generate fake quotes
     */
    public void start(){

    }

    /**
     * stop generate fake quotes
     */
    public void stop(){

    }


}
