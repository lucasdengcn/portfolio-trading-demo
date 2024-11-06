package com.example.demo.market;

import com.example.demo.market.model.Quote;
import com.example.demo.market.pricing.StockPricing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

@Component
public class QuoteProducer implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * simulate event-driven broker
     */
    private final QuoteBroker broker;
    private final StockPool symbolPool;
    private final StockPricing stockPricing;
    private volatile boolean running = false;
    private final Random random = new Random();
    //
    private final ConcurrentLinkedQueue<Quote> quotePool = new ConcurrentLinkedQueue<>();
    //
    @Value("${market.quote-producer-thread.enabled}")
    private boolean enabled;

    /**
     * broker to store latest quotes
     * @param broker
     */
    public QuoteProducer(QuoteBroker broker, StockPool symbolPool, StockPricing stockPricing) {
        this.broker = broker;
        this.symbolPool = symbolPool;
        this.stockPricing = stockPricing;
        // init object pool
        IntStream.range(1, 1000).forEach(value -> quotePool.add(Quote.newBuilder().build()));
        //
    }

    /**
     * start generate fake quotes
     */
    public void start(){
        // pricing
        running = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running){
                    List<String> pocket = symbolPool.randoms();
                    for (String symbol : pocket) {
                        // generate price
                        double price = stockPricing.price();
                        // publish price
                        publishNewPrice(price, symbol);
                    }
                    //
                    int time = 50 + random.nextInt(500);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        thread.setName("QuoteProducer");
        thread.start();
    }

    /**
     *
     * @param price
     * @param symbol
     */
    public void publishNewPrice(double price, String symbol) {
        Quote quote = quotePool.poll();
        Quote.Builder builder;
        if (null == quote){
            builder = Quote.newBuilder();
        } else {
            builder = quote.toBuilder();
        }
        try {
            quote = builder.setPrice(price).setSymbol(symbol).build();
            broker.publish(quote);
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
        running = false;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (enabled) {
            logger.info("QuoteProducer running");
            start();
        }
    }

    @Override
    public void destroy() throws Exception {
        running = false;
        logger.info("QuoteProducer Stopping");
    }

}
