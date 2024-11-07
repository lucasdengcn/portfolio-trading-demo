/* (C) 2024 */ 

package com.example.demo.portfolio.consumer;

import com.example.demo.market.producer.QuoteConsumer;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

public abstract class AbstractQuoteConsumerImpl implements QuoteConsumer, DisposableBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract void handle(ByteString byteString) throws Exception;

    @Override
    public void onEvent(ByteString byteString) {
        try {
            handle(byteString);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
