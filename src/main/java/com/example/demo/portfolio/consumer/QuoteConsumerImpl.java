package com.example.demo.portfolio.consumer;

import com.example.demo.market.ListedStock;
import com.example.demo.market.QuoteBroker;
import com.example.demo.market.QuoteConsumer;
import com.example.demo.market.model.Quote;
import org.springframework.stereotype.Component;

@Component
public class QuoteConsumerImpl implements QuoteConsumer {

    /**
     * simulate event-driven broker
     */
    private final QuoteBroker broker;
    private final ListedStock listedStock;

    public QuoteConsumerImpl(QuoteBroker broker, ListedStock listedStock) {
        this.broker = broker;
        this.listedStock = listedStock;
    }

    public void calculate(Quote quote){

    }

    @Override
    public void onEvent(Quote quote) {
        try {
            calculate(quote);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
