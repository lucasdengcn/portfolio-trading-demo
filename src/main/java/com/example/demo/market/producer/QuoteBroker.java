/* (C) 2024 */ 

package com.example.demo.market.producer;

import com.example.demo.market.model.Quote;
import com.example.demo.market.model.QuoteBatch;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * simulate event-driven broker
 */
@Component
public class QuoteBroker implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //
    private volatile boolean running = false;
    // to simulate broker's message in specific format
    private final LinkedBlockingQueue<ByteString> messages = new LinkedBlockingQueue<>();
    // registration done on startup
    private final List<QuoteConsumer> consumerList = new ArrayList<>();
    //
    @Value("${market.broker-dispatch-thread.enabled:false}")
    private boolean enabled;

    @Value("${market.broker-dispatch-thread.timeout:10}")
    private int waitTimeout;

    public void start() {
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

    public int dispatchMessage() {
        try {
            // wait until having income messages
            ByteString byteString = messages.poll(waitTimeout, TimeUnit.SECONDS);
            if (null != byteString) {
                List<Boolean> list = consumerList.stream()
                        .parallel()
                        .map(consumer -> {
                            consumer.onEvent(byteString);
                            return true;
                        })
                        .collect(Collectors.toList());
                logger.info("QuoteDispatcher dispatch message to {} consumers. ", list.size());
                return 1;
            } else {
                logger.info("QuoteBroker has 0 message to dispatch. ");
                return 0;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return -1;
        }
    }

    /**
     * put message in a queue, and then will notify consumer immediately
     * @param quoteBatch
     */
    public void publish(QuoteBatch quoteBatch) {
        if (quoteBatch.getItemsList().isEmpty()){
            return;
        }
        messages.add(quoteBatch.toByteString());
    }

    /**
     * registration done on startup
     * @param consumer
     */
    public void add(QuoteConsumer consumer) {
        consumerList.add(consumer);
    }

    /**
     * for testing
     * @return
     */
    public ByteString peek() {
        return messages.peek();
    }

    @Override
    public void destroy() throws Exception {
        running = false;
        logger.info("QuoteBroker Stopping");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (enabled) {
            logger.info("QuoteBroker running");
            start();
        }
    }
}
