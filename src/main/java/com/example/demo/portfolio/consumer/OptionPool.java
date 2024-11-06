package com.example.demo.portfolio.consumer;


import com.example.demo.portfolio.entity.ProductType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OptionPool {

    private final ConcurrentHashMap<String, Set<String>> optionMap = new ConcurrentHashMap<>();
    private final ProductType productType;

    public OptionPool(ProductType productType) {
        this.productType = productType;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void registerOption(String symbol){
        String stock = symbol.split("-")[0];
        Set<String> sets = optionMap.get(stock);
        if (null == sets){
            sets = new HashSet<>();
            sets.add(symbol);
        } else {
            sets.add(symbol);
        }
        optionMap.put(stock, sets);
    }

    public Set<String> getOptions(String stock){
        if (optionMap.containsKey(stock)) {
            return optionMap.get(stock);
        }
        return Collections.emptySet();
    }

}
