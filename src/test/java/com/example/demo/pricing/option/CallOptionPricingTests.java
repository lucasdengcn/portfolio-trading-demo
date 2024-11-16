package com.example.demo.pricing.option;

import com.example.demo.model.Option;
import com.example.demo.model.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CallOptionPricingTests {

    @Test
    void valid_parameters() {
        CallOptionPricing pricing = new CallOptionPricing(2);
        Stock stock = Stock.builder()
                .symbol("A")
                .price(100.0f)
                .deviation(0.234)
                .expectedReturn(0.345)
                .build();
        Option option = Option.builder()
                .strikePrice(110.0f)
                .maturity(1)
                .build();
        //
        double price = pricing.price(stock, 101.0f, option);
        Assertions.assertTrue(price > 0);
    }

    @Test
    void invalid_option_strikePrice() {
        CallOptionPricing pricing = new CallOptionPricing(2);
        Stock stock = Stock.builder()
                .symbol("A")
                .price(100.0f)
                .deviation(0.234)
                .expectedReturn(0.345)
                .build();
        Option option = Option.builder()
                .strikePrice(-110.0f)
                .maturity(1)
                .build();
        //
        Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                pricing.price(stock, 101.0f, option);
            }
        });
    }

    @Test
    void invalid_option_maturity() {
        CallOptionPricing pricing = new CallOptionPricing(2);
        Stock stock = Stock.builder()
                .symbol("A")
                .price(100.0f)
                .deviation(0.234)
                .expectedReturn(0.345)
                .build();
        Option option = Option.builder()
                .strikePrice(-110.0f)
                .maturity(-1)
                .build();
        //
        Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                pricing.price(stock, 101.0f, option);
            }
        });
    }
}