package com.example.demo.market;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ListedStock {

    private final Set<String> symbols = new HashSet<>();

    public void registerSymbols(List<String> symbols){
        this.symbols.addAll(symbols);
    }

    public void registerSymbol(String symbol){
        this.symbols.add(symbol);
    }

    public Set<String> getSymbols() {
        return symbols;
    }

    public int count(){
        return symbols.size();
    }

}
