/* (C) 2024 */ 

package com.example.demo.market.producer;

import com.example.demo.market.model.Stock;
import com.google.common.collect.Lists;
import java.util.*;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

@Component
public class StockPool {
    // init on startup
    private final List<String> stocks = new ArrayList<>();
    private final Map<String, Stock> stockPrice = Maps.newConcurrentMap();
    private final Random random = new Random();
    //
    private final List<String> pocket = Lists.newArrayList();

    public void register(String symbol) {
        if (this.stocks.contains(symbol)) return;
        this.stocks.add(symbol);
    }

    public void registerPrice(Stock stock){
        stockPrice.put(stock.getSymbol(), stock);
    }

    public void updatePrice(String symbol, double price){
        Stock stock = stockPrice.get(symbol);
        Stock build = stock.toBuilder().setPrice(price).build();
        stockPrice.put(symbol, build);
    }

    public Stock getLatestPrice(String symbol){
        return stockPrice.get(symbol);
    }

    public List<String> getStocks() {
        return stocks;
    }

    public int countOfStock() {
        return stocks.size();
    }

    public boolean contains(String symbol) {
        return stocks.contains(symbol);
    }

    public String random() {
        int count = stocks.size();
        int index = random.nextInt(count);
        return stocks.get(index);
    }

    public List<String> randoms() {
        pocket.clear();
        int changes = 1 + random.nextInt(2);
        for (int i = 0; i < changes; i++) {
            String symbol = random();
            pocket.add(symbol);
        }
        return pocket;
    }

}
