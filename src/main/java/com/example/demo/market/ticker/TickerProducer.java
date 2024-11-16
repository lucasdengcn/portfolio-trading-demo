/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.ticker;

import com.example.demo.broker.DataBroker;
import com.example.demo.market.stock.StockPool;
import com.example.demo.model.Stock;
import com.example.demo.model.Ticker;
import com.example.demo.pricing.stock.StockPricing;
import java.time.Duration;
import java.util.Random;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import stormpot.Pooled;

@Component
public class TickerProducer implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataBroker broker;
    private final StockPool stockPool;
    private final StockPricing stockPricing;
    private final TickerObjectPool tickerObjectPool;
    /**
     *
     */
    private volatile boolean running = false;

    private final Random random = new Random();
    /**
     *
     */
    @Value("${app.ticker.producer.enabled:false}")
    private boolean enabled;

    /**
     * broker to store latest quotes
     * @param broker
     */
    public TickerProducer(DataBroker broker, StockPool stockPool, StockPricing stockPricing) {
        this.broker = broker;
        this.stockPool = stockPool;
        this.stockPricing = stockPricing;
        this.tickerObjectPool = new TickerObjectPool(10);
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
                int timeInMs = randomTimeInMs();
                while (running) {
                    Set<String> symbols = stockPool.randoms();
                    publishNewPricesInTime(symbols, timeInMs);
                    // sleep then continue generating latest price
                    timeInMs = randomTimeInMs();
                    try {
                        Thread.sleep(timeInMs);
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
     * @param symbols
     * @param time
     */
    public void publishNewPricesInTime(@NonNull Set<String> symbols, int time) {
        if (!symbols.isEmpty()) {
            try {
                for (String symbol : symbols) {
                    // generate price
                    Stock stock = stockPool.getOne(symbol);
                    double price = stockPricing.price(stock, Duration.ofMillis(time));
                    stockPool.updatePrice(symbol, price);
                    //
                    this.publishNewPrice(symbol, price);
                    //
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private int randomTimeInMs() {
        return 500 + random.nextInt(1500);
    }

    /**
     *
     * @param price
     * @param symbol
     */
    public void publishNewPrice(String symbol, double price) {
        try (Pooled<Ticker> pooled = tickerObjectPool.borrowObject()) {
            Ticker ticker = pooled.object;
            ticker.setSymbol(symbol);
            ticker.setPrice(price);
            ticker.setVolume(1);
            this.broker.publish(ticker);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (enabled) {
            logger.info("TickerProducer running");
            start();
        }
    }

    @Override
    public void destroy() throws Exception {
        running = false;
        logger.info("TickerProducer Stopping");
    }
}
