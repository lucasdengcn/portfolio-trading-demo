/* (C) 2024 */ 

package com.example.demo.portfolio.service;

import com.example.demo.portfolio.model.SymbolType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OptionPool {

    private final ConcurrentHashMap<String, Set<String>> optionMap = new ConcurrentHashMap<>();
    private final SymbolType symbolType;

    public OptionPool(SymbolType symbolType) {
        this.symbolType = symbolType;
    }

    public SymbolType getProductType() {
        return symbolType;
    }

    public void registerOption(String symbol) {
        String stock = symbol.split("-")[0];
        Set<String> sets = optionMap.get(stock);
        if (null == sets) {
            sets = new HashSet<>();
            sets.add(symbol);
        } else {
            sets.add(symbol);
        }
        optionMap.put(stock, sets);
    }

    public Set<String> getOptions(String stock) {
        if (optionMap.containsKey(stock)) {
            return optionMap.get(stock);
        }
        return Collections.emptySet();
    }

    public void clear() {
        optionMap.clear();
    }
}
