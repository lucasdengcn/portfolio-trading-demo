/* (C) 2024 */ 

package com.example.demo.market.stock.pricing;

import com.example.demo.market.model.Stock;
import java.time.Duration;
import org.springframework.lang.NonNull;

public interface StockPricing {

    double price(@NonNull Stock stock, @NonNull Duration duration);
}
