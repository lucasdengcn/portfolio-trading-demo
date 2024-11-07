/* (C) 2024 */ 

package com.example.demo.market.option;

import com.example.demo.market.model.Option;
import java.util.concurrent.ConcurrentHashMap;

public class OptionPool {

    private final ConcurrentHashMap<String, Option> optionMap = new ConcurrentHashMap<>();

    public void register(Option option) {
        optionMap.put(option.getSymbol(), option);
    }

    public Option getOption(String symbol) {
        return optionMap.get(symbol);
    }

    public void clear() {
        optionMap.clear();
    }

    public int count() {
        return optionMap.size();
    }
}
