/* (C) 2024 */ 

package com.example.demo.market;

import com.example.demo.market.stock.pricing.StockPricing;
import com.example.demo.market.stock.pricing.impl.GeometricDiscretePricing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarketConfiguration {

    @Bean
    public StockPricing stockPricing() {
        return new GeometricDiscretePricing();
    }
}
