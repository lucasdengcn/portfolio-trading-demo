/* (C) 2024 */ 

package com.example.demo.market.option.pricing;

import com.example.demo.market.model.Option;
import com.example.demo.market.model.Quote;
import com.example.demo.market.model.Stock;

/**
 * calculator to calculate option's price
 */
public interface OptionPricing {

    double price(Stock stock, Quote stockQuote, Option option);
}
