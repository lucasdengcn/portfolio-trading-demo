/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.broker;

import com.example.demo.market.ticker.TickerObjectPool;
import com.example.demo.model.Ticker;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import stormpot.Pooled;

/**
 * simulate event-driven broker
 */
@Component
public class DataBroker implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //
    private volatile boolean running = false;
    /**
     *
     */
    private final LinkedBlockingQueue<Ticker> messages = new LinkedBlockingQueue<>();
    /**
     *
     */
    private final List<TickerConsumer> consumerList = new ArrayList<>();
    /**
     *
     */
    @Value("${app.broker.dispatcher.enabled:false}")
    private boolean enabled;
    /**
     *
     */
    @Value("${app.broker.dispatcher.timeout:10}")
    private int waitTimeout;

    private final TickerObjectPool tickerObjectPool;

    public DataBroker() {
        this.tickerObjectPool = new TickerObjectPool(100_000);
    }

    private void start() {
        running = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    dispatchMessage();
                }
                logger.info("QuoteDispatcher exist.");
            }
        });
        thread.setName("QuoteDispatcher");
        thread.start();
    }

    public void clear() {
        messages.clear();
    }

    /**
     *
     * dispatch message to consumer.
     *
     * @return
     */
    public int dispatchMessage() {
        try {
            // wait until having income messages
            Ticker pooled = messages.poll(waitTimeout, TimeUnit.SECONDS);
            if (null != pooled) {
                List<Boolean> list = consumerList.stream()
                        .parallel()
                        .map(consumer -> {
                            consumer.onEvent(pooled);
                            return true;
                        })
                        .collect(Collectors.toList());
                logger.info("Ticker Dispatcher dispatch message to {} consumers. ", list.size());
                return 1;
            } else {
                logger.info("DataBroker has 0 message to dispatch. ");
                return 0;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return -1;
        }
    }

    /**
     * put message in a queue, and then will notify consumer immediately
     */
    public boolean publish(Ticker newTick) {
        if (null == newTick || StringUtils.isEmpty(newTick.getSymbol())) {
            return false;
        }
        // utilize object pool to reuse object to avoid GC times
        try (Pooled<Ticker> pooled = tickerObjectPool.borrowObject()) {
            Ticker ticker = pooled.object;
            ticker.setSymbol(newTick.getSymbol());
            ticker.setPrice(newTick.getPrice());
            ticker.setVolume(newTick.getVolume());
            //
            return this.messages.add(ticker);
        }
    }

    /**
     * registration done on startup
     * @param consumer
     */
    public void add(@NonNull TickerConsumer consumer) {
        consumerList.add(consumer);
    }

    /**
     * for testing
     * @return
     */
    public Ticker peek() {
        return messages.peek();
    }

    @Override
    public void destroy() throws Exception {
        running = false;
        logger.info("DataBroker Stopping");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (enabled) {
            logger.info("DataBroker running");
            start();
        }
    }
}
