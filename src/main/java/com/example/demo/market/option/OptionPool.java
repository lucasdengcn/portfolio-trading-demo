/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.option;

import com.example.demo.model.Option;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.lang.NonNull;

public class OptionPool {

    private final ConcurrentHashMap<String, Option> optionMap = new ConcurrentHashMap<>();

    public void register(@NonNull Option option) {
        optionMap.put(option.getSymbol(), option);
    }

    public Option getOption(@NonNull String symbol) {
        return optionMap.get(symbol);
    }

    public void clear() {
        optionMap.clear();
    }

    public int count() {
        return optionMap.size();
    }
}
