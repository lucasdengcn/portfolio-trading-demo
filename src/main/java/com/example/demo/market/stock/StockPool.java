/* (C) 2024 */ 

package com.example.demo.market.stock;

import com.example.demo.market.model.Stock;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StockPool {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // init on startup
    private final List<String> stocksSymbols = new ArrayList<>();
    // tracking stock latest price
    private final Map<String, Stock> stockPrice = Maps.newConcurrentMap();
    private final Random random = new Random();
    //
    public void register(@NonNull String symbol) {
        if (this.stocksSymbols.contains(symbol)) return;
        this.stocksSymbols.add(symbol);
    }

    public void registerPrice(@NonNull Stock stock) {
        stockPrice.put(stock.getSymbol(), stock);
        register(stock.getSymbol());
    }

    public void updatePrice(@NonNull String symbol, double price) {
        Stock stock = stockPrice.get(symbol);
        Stock build = stock.toBuilder().setPrice(price).build();
        stockPrice.put(symbol, build);
    }

    public Stock getLatestPrice(@NonNull String symbol) {
        return stockPrice.get(symbol);
    }

    public List<String> getStocks() {
        return stocksSymbols;
    }

    public int countOfStock() {
        return stocksSymbols.size();
    }

    public boolean contains(@NonNull String symbol) {
        return stocksSymbols.contains(symbol);
    }

    public String random() {
        int count = stocksSymbols.size();
        logger.debug("stockSymbols, size: {}", count);
        int index = random.nextInt(count);
        return stocksSymbols.get(index);
    }

    public Set<String> randoms() {
        Set<String> pocket = Sets.newHashSet();
        if (!stocksSymbols.isEmpty()) {
            int changes = 1 + random.nextInt(2);
            for (int i = 0; i < changes; i++) {
                String symbol = random();
                pocket.add(symbol);
            }
        }
        return pocket;
    }
}
