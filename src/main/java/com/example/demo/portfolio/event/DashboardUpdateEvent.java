/* (C) 2024 */ 

package com.example.demo.portfolio.event;

import com.example.demo.market.model.Quote;
import com.example.demo.portfolio.model.Portfolio;
import java.util.List;
import java.util.Set;

public class DashboardUpdateEvent {

    private final Set<Quote> quoteSet;
    private final List<Portfolio> portfolio;

    public DashboardUpdateEvent(Set<Quote> quoteSet, List<Portfolio> portfolio) {
        this.quoteSet = quoteSet;
        this.portfolio = portfolio;
    }

    public Set<Quote> getQuoteSet() {
        return quoteSet;
    }

    public List<Portfolio> getPortfolio() {
        return portfolio;
    }
}
