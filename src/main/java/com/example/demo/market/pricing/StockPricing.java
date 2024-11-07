/* (C) 2024 */ 

package com.example.demo.market.pricing;

import com.example.demo.market.model.Stock;
import java.time.Duration;

public interface StockPricing {

    double price(Stock stock, Duration duration);
}
