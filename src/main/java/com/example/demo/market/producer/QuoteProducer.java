/* (C) 2024 */ 

package com.example.demo.market.producer;

import com.example.demo.market.model.Quote;
import com.example.demo.market.model.Stock;
import com.example.demo.market.pricing.StockPricing;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QuoteProducer implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * simulate event-driven broker
     */
    private final QuoteBroker broker;

    private final StockPool stockPool;
    private final StockPricing stockPricing;
    private volatile boolean running = false;
    private final Random random = new Random();
    //
    private final ConcurrentLinkedQueue<Quote.Builder> quotePool = new ConcurrentLinkedQueue<>();
    //
    @Value("${market.quote-producer-thread.enabled:false}")
    private boolean enabled;

    /**
     * broker to store latest quotes
     * @param broker
     */
    public QuoteProducer(QuoteBroker broker, StockPool stockPool, StockPricing stockPricing) {
        this.broker = broker;
        this.stockPool = stockPool;
        this.stockPricing = stockPricing;
        // init object pool
        IntStream.range(1, 1000).forEach(value -> quotePool.add(Quote.newBuilder()));
        //
    }

    /**
     * start generate fake quotes
     */
    private void start() {
        // pricing
        running = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int time = randomTime();
                while (running) {
                    List<String> pocket = stockPool.randoms();
                    for (String symbol : pocket) {
                        // generate price
                        Stock stock = stockPool.getLatestPrice(symbol);
                        double price = stockPricing.price(stock, Duration.ofMillis(time));
                        // publish price
                        publishNewPrice(price, symbol);
                        //
                        stockPool.updatePrice(symbol, price);
                    }
                    // sleep then continue generating latest price
                    time = randomTime();
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

    private int randomTime() {
        return 500 + random.nextInt(1500);
    }

    /**
     *
     * @param price
     * @param symbol
     */
    public void publishNewPrice(double price, String symbol) {
        Quote.Builder builder = quotePool.poll();
        if (null == builder) {
            builder = Quote.newBuilder();
        }
        try {
            // quote is immutable object, it is thread-safe
            Quote quote = builder.setPrice(price).setSymbol(symbol).build();
            broker.publish(quote);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            quotePool.add(builder);
        }
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
