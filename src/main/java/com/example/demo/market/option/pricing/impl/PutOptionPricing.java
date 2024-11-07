/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.option.pricing.impl;

import com.example.demo.market.model.Option;
import com.example.demo.market.model.Quote;
import com.example.demo.market.model.Stock;
import com.example.demo.market.option.pricing.OptionPricing;
import com.example.demo.market.utils.Norm;
import org.springframework.lang.NonNull;

public class PutOptionPricing implements OptionPricing {

    private final double riskFreeInterestRate;

    public PutOptionPricing(double riskFreeInterestRate) {
        this.riskFreeInterestRate = riskFreeInterestRate;
    }

    @Override
    public double price(@NonNull Stock stock, @NonNull Quote stockQuote, @NonNull Option option) {
        double stockPrice = stockQuote.getPrice();
        double strikePrice = option.getStrikePrice();
        double r = this.riskFreeInterestRate;
        int maturity = option.getMaturity();
        double sigma = stock.getDeviation();
        //
        double d1 = (Math.log(stockPrice / strikePrice) + (r + Math.pow(sigma, 2) / 2) * maturity)
                / (sigma * Math.sqrt(maturity));
        double d2 = d1 - sigma * Math.sqrt(maturity);
        double cdfd1 = Norm.cdf(-d1);
        double cdfd2 = Norm.cdf(-d2);
        //
        return strikePrice * Math.exp(-r * maturity) * cdfd2 - stockPrice * cdfd1;
    }
}
