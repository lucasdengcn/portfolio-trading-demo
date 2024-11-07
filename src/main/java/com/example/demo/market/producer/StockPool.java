/* (C) 2024 */ 

package com.example.demo.market.producer;

import com.example.demo.market.model.Stock;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class StockPool {
    // init on startup
    private final List<String> stocksSymbols = new ArrayList<>();
    private final Map<String, Stock> stockPrice = Maps.newConcurrentMap();
    private final Random random = new Random();
    //
    private final List<String> pocket = Lists.newArrayList();

    public void register(String symbol) {
        if (this.stocksSymbols.contains(symbol)) return;
        this.stocksSymbols.add(symbol);
    }

    public void registerPrice(Stock stock) {
        stockPrice.put(stock.getSymbol(), stock);
        register(stock.getSymbol());
    }

    public void updatePrice(String symbol, double price) {
        Stock stock = stockPrice.get(symbol);
        Stock build = stock.toBuilder().setPrice(price).build();
        stockPrice.put(symbol, build);
    }

    public Stock getLatestPrice(String symbol) {
        return stockPrice.get(symbol);
    }

    public List<String> getStocks() {
        return stocksSymbols;
    }

    public int countOfStock() {
        return stocksSymbols.size();
    }

    public boolean contains(String symbol) {
        return stocksSymbols.contains(symbol);
    }

    public String random() {
        int count = stocksSymbols.size();
        int index = random.nextInt(count);
        return stocksSymbols.get(index);
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
