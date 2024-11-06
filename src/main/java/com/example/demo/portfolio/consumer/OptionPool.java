package com.example.demo.portfolio.consumer;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OptionPool {

    private final ConcurrentHashMap<String, Set<String>> optionMap = new ConcurrentHashMap<>();

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
        return optionMap.get(stock);
    }

}
