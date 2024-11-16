/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.stock;

import com.example.demo.model.Stock;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StockPool {
    /**
     *
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     *
     */
    private final List<String> stocksSymbols = new ArrayList<>();
    /**
     *
     */
    private final Map<String, Stock> stockPrice = Maps.newConcurrentMap();
    /**
     *
     */
    private final Random random = new Random();

    /**
     *
     * @param stock
     */
    public void registerPrice(@NonNull Stock stock) {
        stockPrice.put(stock.getSymbol(), stock);
        if (this.stocksSymbols.contains(stock.getSymbol())) return;
        this.stocksSymbols.add(stock.getSymbol());
    }

    /**
     *
     * @param symbol
     * @param price
     */
    public void updatePrice(@NonNull String symbol, double price) {
        Stock stock = stockPrice.get(symbol);
        stock.setPrice(price);
        stockPrice.put(symbol, stock);
    }

    /**
     *
     * @param symbol
     * @return
     */
    public Stock getOne(@NonNull String symbol) {
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
