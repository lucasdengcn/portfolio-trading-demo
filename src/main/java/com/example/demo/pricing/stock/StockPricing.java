/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.pricing.stock;

import com.example.demo.model.Stock;
import java.time.Duration;
import org.springframework.lang.NonNull;

public interface StockPricing {

    double price(@NonNull Stock stock, @NonNull Duration duration);
}
