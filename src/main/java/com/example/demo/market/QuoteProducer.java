package com.example.demo.market;

import com.example.demo.market.model.Quote;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

@Component
public class QuoteProducer {

    /**
     * simulate event-driven broker
     */
    private final QuoteBroker broker;
    private final StockPool symbolPool;
    private ConcurrentLinkedQueue<Quote> quotePool = new ConcurrentLinkedQueue<>();

    /**
     * broker to store latest quotes
     * @param broker
     */
    public QuoteProducer(QuoteBroker broker, StockPool symbolPool) {
        this.broker = broker;
        this.symbolPool = symbolPool;
        // object pool
        IntStream.range(1, 10).forEach(value -> quotePool.add(Quote.newBuilder().build()));
    }

    /**
     * start generate fake quotes
     */
    public void start(){
        // random price
        Quote quote = quotePool.poll();
        if (null == quote){
            return;
        }
        try {
            this.broker.publish(quote);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            quotePool.add(quote);
        }
    }

    /**
     * stop generate fake quotes
     */
    public void stop(){

    }


}
