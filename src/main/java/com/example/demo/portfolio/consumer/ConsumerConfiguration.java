/* (C) 2024 */ 

package com.example.demo.portfolio.consumer;

import com.example.demo.market.producer.QuoteBroker;
import com.example.demo.market.producer.StockPool;
import com.example.demo.portfolio.entity.ProductType;
import com.example.demo.portfolio.service.PositionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfiguration {

    @Bean("callOptionPool")
    public OptionPool callOptionPool() {
        return new OptionPool(ProductType.CALL);
    }

    @Bean("putOptionPool")
    public OptionPool putOptionPool() {
        return new OptionPool(ProductType.PUT);
    }

    @Bean("quoteConsumer")
    public QuoteConsumerImpl callOptionConsumer(
            OptionPool callOptionPool,
            QuoteBroker quoteBroker,
            OptionPool putOptionPool,
            StockPool stockPool,
            PositionService positionService) {
        QuoteConsumerImpl consumer = new QuoteConsumerImpl(callOptionPool, putOptionPool, stockPool, positionService);
        quoteBroker.add(consumer);
        return consumer;
    }
}
