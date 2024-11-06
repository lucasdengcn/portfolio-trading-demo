package com.example.demo.market;

import com.example.demo.market.pricing.StockPricing;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StockPool {

    private final List<String> stocks = Collections.synchronizedList(new ArrayList<>());
    private final Random random = new Random();
    private final List<String> pocket = Lists.newArrayList();

    public void registerSymbol(String symbol){
        if (this.stocks.contains(symbol))
            return;
        this.stocks.add(symbol);
    }

    public List<String> getStocks() {
        return stocks;
    }

    public int countOfStock(){
        return stocks.size();
    }

    public boolean contains(String symbol){
        return stocks.contains(symbol);
    }

    public String random(){
        int count = stocks.size();
        int index = random.nextInt(count);
        return stocks.get(index);
    }

    public List<String> randoms(){
        pocket.clear();
        int changes = 1 + random.nextInt(2);
        for (int i = 0; i < changes; i++) {
            String symbol = random();
            pocket.add(symbol);
        }
        return pocket;
    }

}
