/* (C) 2024 */ 

package com.example.demo.portfolio;

import com.example.demo.market.producer.QuoteBroker;
import com.example.demo.market.producer.StockPool;
import com.example.demo.portfolio.consumer.QuoteConsumerImpl;
import com.example.demo.portfolio.service.PositionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortfolioConfiguration {

    @Bean("quoteConsumer")
    public QuoteConsumerImpl callOptionConsumer(
            QuoteBroker quoteBroker, StockPool stockPool, PositionService positionService) {
        QuoteConsumerImpl consumer = new QuoteConsumerImpl(stockPool, positionService);
        quoteBroker.add(consumer);
        return consumer;
    }
}
