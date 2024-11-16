/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.pricing.option;

import com.example.demo.math.Norm;
import com.example.demo.model.Option;
import com.example.demo.model.Stock;
import org.springframework.lang.NonNull;

public class PutOptionPricing implements OptionPricing {

    private final double riskFreeInterestRate;

    public PutOptionPricing(double riskFreeInterestRate) {
        if (riskFreeInterestRate <= 0){
            throw new IllegalArgumentException("r should be larger than 0");
        }
        this.riskFreeInterestRate = riskFreeInterestRate;
    }

    @Override
    public double price(@NonNull Stock stock, @NonNull double stockPrice, @NonNull Option option) {
        double strikePrice = option.getStrikePrice();
        int maturity = option.getMaturity();
        if (strikePrice <= 0 || maturity <= 0){
            throw new IllegalArgumentException("strikePrice or maturity should be larger than 0");
        }
        double r = this.riskFreeInterestRate;
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
