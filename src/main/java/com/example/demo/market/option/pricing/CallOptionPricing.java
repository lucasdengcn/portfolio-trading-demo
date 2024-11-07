/* (C) 2024 */ 

package com.example.demo.market.option.pricing;

import com.example.demo.market.model.Option;
import com.example.demo.market.model.Quote;
import com.example.demo.market.model.Stock;

public class CallOptionPricing implements OptionPricing {

    private final double riskFreeInterestRate;

    public CallOptionPricing(double riskFreeInterestRate) {
        this.riskFreeInterestRate = riskFreeInterestRate;
    }

    @Override
    public double price(Stock stock, Quote stockQuote, Option option) {
        //
        double stockPrice = stockQuote.getPrice();
        double strikePrice = option.getStrikePrice();
        double r = this.riskFreeInterestRate;
        int maturity = option.getMaturity();
        double sigma = stock.getDeviation();
        //
        double d1 = (Math.log(stockPrice / strikePrice) + (r + Math.pow(sigma, 2) / 2) * maturity)
                / (sigma * Math.sqrt(maturity));
        double d2 = d1 - sigma * Math.sqrt(maturity);
        double cdfd1 = Norm.cdf(d1);
        double cdfd2 = Norm.cdf(d2);
        //
        return stockPrice * cdfd1 - strikePrice * Math.exp(-r * maturity) * cdfd2;
    }
}
