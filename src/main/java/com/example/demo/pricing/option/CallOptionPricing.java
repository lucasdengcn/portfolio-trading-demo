/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.pricing.option;

import com.example.demo.math.Norm;
import com.example.demo.messaging.model.Quote;
import com.example.demo.model.Option;
import com.example.demo.model.Stock;
import org.springframework.lang.NonNull;

public class CallOptionPricing implements OptionPricing {

    private final double riskFreeInterestRate;

    public CallOptionPricing(double riskFreeInterestRate) {
        this.riskFreeInterestRate = riskFreeInterestRate;
    }

    @Override
    public double price(@NonNull Stock stock, @NonNull Quote stockQuote, @NonNull Option option) {
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
