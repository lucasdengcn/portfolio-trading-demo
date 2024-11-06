package com.example.demo.market;

import com.example.demo.portfolio.entity.ProductType;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StockPool {

    private final Set<String> stocks = new HashSet<>();

    public void registerSymbol(String symbol){
        this.stocks.add(symbol);
    }

    public Set<String> getStocks() {
        return stocks;
    }

    public int countOfStock(){
        return stocks.size();
    }

}
