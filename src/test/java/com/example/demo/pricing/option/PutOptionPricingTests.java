package com.example.demo.pricing.option;

import com.example.demo.model.Option;
import com.example.demo.model.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class PutOptionPricingTests {

    @Test
    void given_rising_stock_price_put_price() {
        PutOptionPricing pricing = new PutOptionPricing(2);
        Stock stock = Stock.builder()
                .symbol("A")
                .price(100.0f)
                .deviation(0.234)
                .expectedReturn(0.345)
                .build();
        Option option = Option.builder()
                .strikePrice(99.0f)
                .maturity(1)
                .build();
        //
        double price = pricing.price(stock, 101.0f, option);
        Assertions.assertTrue(price < 0);
    }

    @Test
    void given_dropped_stock_price_put_price() {
        PutOptionPricing pricing = new PutOptionPricing(2);
        Stock stock = Stock.builder()
                .symbol("A")
                .price(100.0f)
                .deviation(0.234)
                .expectedReturn(0.345)
                .build();
        Option option = Option.builder()
                .strikePrice(90.0f)
                .maturity(1)
                .build();
        //
        double price = pricing.price(stock, 95.0f, option);
        System.out.println(price);
        Assertions.assertTrue(price < 0);
    }

    @Test
    void invalid_option_strikePrice() {
        PutOptionPricing pricing = new PutOptionPricing(2);
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
        PutOptionPricing pricing = new PutOptionPricing(2);
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