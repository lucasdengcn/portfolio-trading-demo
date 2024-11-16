/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.consumer;

import com.example.demo.broker.TickerConsumer;
import com.example.demo.model.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;

public abstract class AbstractTickerConsumerImpl implements TickerConsumer, InitializingBean, DisposableBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract void handle(@NonNull Ticker ticker) throws Exception;

    @Override
    public void onEvent(@NonNull Ticker ticker) {
        try {
            handle(ticker);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
