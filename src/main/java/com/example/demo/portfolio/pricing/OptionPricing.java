/* (C) 2024 */ 

package com.example.demo.portfolio.pricing;

import com.example.demo.market.model.Quote;
import com.example.demo.portfolio.model.Position;

/**
 * calculator to calculate option's price
 */
public interface OptionPricing {

    double price(Quote stockQuote, Position position);
}
