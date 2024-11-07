package com.example.demo.portfolio.pricing;

import com.example.demo.market.model.Quote;
import com.example.demo.portfolio.model.Position;

public class PutOptionPricing implements OptionPricing {

    @Override
    public double price(Quote stockQuote, Position position) {
        return stockQuote.getPrice();
    }

}
