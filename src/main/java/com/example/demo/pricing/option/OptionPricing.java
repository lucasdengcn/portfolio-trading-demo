/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.pricing.option;

import com.example.demo.model.Option;
import com.example.demo.model.Stock;
import org.springframework.lang.NonNull;

/**
 * calculator to calculate option's price
 */
public interface OptionPricing {

    double price(@NonNull Stock stock, @NonNull double price, @NonNull Option option);
}
