/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.pricing.option;

import com.example.demo.messaging.model.Quote;
import com.example.demo.model.Option;
import com.example.demo.model.Stock;

/**
 * calculator to calculate option's price
 */
public interface OptionPricing {

    double price(Stock stock, Quote stockQuote, Option option);
}
