package com.example.demo.portfolio.consumer;

import com.example.demo.market.QuoteConsumer;
import com.example.demo.market.model.Quote;
import com.google.protobuf.ByteString;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;


public abstract class AbstractOptionConsumerImpl implements QuoteConsumer {

    protected final OptionPool optionPool;
    protected final ConcurrentLinkedQueue<Quote> quotePool = new ConcurrentLinkedQueue<>();

    public AbstractOptionConsumerImpl(OptionPool optionPool) {
        this.optionPool = optionPool;
        IntStream.range(1, 10).forEach(value -> quotePool.add(Quote.newBuilder().build()));
    }

    public abstract void calculate(Quote stockQuote);

    @Override
    public void onEvent(ByteString byteString) {
        try {
            calculate(Quote.parseFrom(byteString));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
