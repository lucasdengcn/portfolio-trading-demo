package com.example.demo.portfolio.pricing;

import com.example.demo.market.model.Quote;
import com.example.demo.portfolio.model.Position;
import org.springframework.stereotype.Component;

@Component
public class CallOptionPricing implements OptionPricing {

    @Override
    public double price(Quote stockQuote, Position position) {
        return 0;
    }

}
