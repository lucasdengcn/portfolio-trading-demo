/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market;

import com.example.demo.pricing.stock.StockGeometricDiscretePricing;
import com.example.demo.pricing.stock.StockPricing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarketConfiguration {

    @Bean
    public StockPricing stockPricing() {
        return new StockGeometricDiscretePricing();
    }
}
