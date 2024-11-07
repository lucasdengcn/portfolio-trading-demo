/* (C) 2024 */ 

package com.example.demo.market.producer;

import com.example.demo.market.model.Quote;
import com.example.demo.market.model.QuoteBatch;
import com.example.demo.market.model.Stock;
import com.example.demo.market.stock.StockPool;
import com.example.demo.market.stock.pricing.StockPricing;
import java.time.Duration;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
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
    // object pooling
    private final ConcurrentLinkedQueue<Quote.Builder> quotePool = new ConcurrentLinkedQueue<>();
    // if running in multiple instance, would need to corresponding instance
    private final QuoteBatch.Builder quoteBatchBuilder = QuoteBatch.newBuilder();
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
        // init object pool, 1000 for demo purpose
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
                    Set<String> pocket = stockPool.randoms();
                    generatePublishPrice(pocket, time);
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

    /**
     *
     * @param pocket
     * @param time
     */
    public void generatePublishPrice(@NonNull Set<String> pocket, int time) {
        if (null != pocket && !pocket.isEmpty()) {
            try {
                for (String symbol : pocket) {
                    // generate price
                    Stock stock = stockPool.getLatestPrice(symbol);
                    double price = stockPricing.price(stock, Duration.ofMillis(time));
                    stockPool.updatePrice(symbol, price);
                    wrapNewPrice(price, symbol);
                }
                //
                broker.publish(quoteBatchBuilder.build());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                quoteBatchBuilder.clear();
            }
        }
    }

    private int randomTime() {
        return 1000 + random.nextInt(1500);
    }

    /**
     *
     * @param price
     * @param symbol
     */
    private void wrapNewPrice(double price, String symbol) {
        Quote.Builder builder = quotePool.poll();
        if (null == builder) {
            builder = Quote.newBuilder();
        }
        try {
            // quote is immutable object, it is thread-safe
            Quote quote = builder.setPrice(price).setSymbol(symbol).build();
            quoteBatchBuilder.addItems(quote);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            builder.clear();
            quotePool.add(builder);
        }
    }

    /**
     * for testing
     *
     * @param symbol
     * @param price
     */
    public void publishNewPrice(@NonNull String symbol, double price) {
        try {
            wrapNewPrice(price, symbol);
            broker.publish(quoteBatchBuilder.build());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            quoteBatchBuilder.clear();
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
