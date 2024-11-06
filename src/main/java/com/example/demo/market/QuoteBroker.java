package com.example.demo.market;

import com.example.demo.market.model.Quote;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * simulate event-driven broker
 */
@Component
public class QuoteBroker implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //
    private volatile boolean running = false;
    private final LinkedBlockingQueue<ByteString> messages = new LinkedBlockingQueue <>();
    private final List<QuoteConsumer> consumerList = new ArrayList<>();
    //
    @Value("${market.broker-dispatch-thread.enabled}")
    private boolean enabled;

    @Value("${market.broker-dispatch-thread.timeout}")
    private int waitTimeout = 10;

    public void start(){
        running = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running){
                    dispatchMessage();
                }
                logger.info("QuoteDispatcher exist.");
            }
        });
        thread.setName("QuoteDispatcher");
        thread.start();
    }

    public void clear(){
        messages.clear();
    }

    public int dispatchMessage(){
        try{
            ByteString byteString = messages.poll(waitTimeout, TimeUnit.SECONDS);
            if (null != byteString) {
                List<Boolean> list = consumerList.stream().parallel().map(consumer -> {
                    consumer.onEvent(byteString);
                    return true;
                }).collect(Collectors.toList());
                logger.info("QuoteDispatcher dispatch message to {} consumers. ", list.size());
                return 1;
            } else {
                logger.info("QuoteBroker has 0 message to dispatch. ");
                return 0;
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return -1;
        }
    }

    public void publish(Quote quote){
        messages.add(quote.toByteString());
    }

    public void add(QuoteConsumer consumer){
        consumerList.add(consumer);
    }

    public ByteString peek(){
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
