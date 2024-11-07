package com.example.demo.portfolio.service;

import com.example.demo.portfolio.model.SymbolType;
import com.example.demo.portfolio.pricing.CallOptionPricing;
import com.example.demo.portfolio.pricing.OptionPricing;
import com.example.demo.portfolio.pricing.PutOptionPricing;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class OptionManager {

    private final Map<SymbolType, OptionPool> optionPoolMap = Maps.newHashMap();
    private final Map<SymbolType, OptionPricing> optionPricingMap = Maps.newHashMap();

    public OptionManager() {
        optionPoolMap.put(SymbolType.PUT, new OptionPool(SymbolType.PUT));
        optionPoolMap.put(SymbolType.CALL, new OptionPool(SymbolType.CALL));
        //
        optionPricingMap.put(SymbolType.CALL, new CallOptionPricing());
        optionPricingMap.put(SymbolType.PUT, new PutOptionPricing());
    }

    /**
     *
     * @param type
     * @param symbol
     */
    public void register(SymbolType type, String symbol){
        optionPoolMap.get(type).registerOption(symbol);
    }

    public Set<String> findSymbols(String stock){
        return optionPoolMap.values().stream()
                .map(optionPool -> optionPool.getOptions(stock))
                .flatMap((Function<Set<String>, Stream<String>>) Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<String> findSymbols(SymbolType type, String stock){
        return optionPoolMap.get(type).getOptions(stock);
    }

    public OptionPricing getPricing(SymbolType type){
        return optionPricingMap.get(type);
    }

    public void clear(){
        optionPoolMap.values().forEach(OptionPool::clear);
    }

}
