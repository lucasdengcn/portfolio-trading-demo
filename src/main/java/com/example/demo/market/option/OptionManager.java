/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.option;

import com.example.demo.model.Option;
import com.example.demo.model.SymbolType;
import com.example.demo.pricing.option.CallOptionPricing;
import com.example.demo.pricing.option.OptionPricing;
import com.example.demo.pricing.option.PutOptionPricing;
import com.google.common.collect.Maps;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OptionManager {

    @Getter
    private final OptionPool optionPool = new OptionPool();
    //
    private final Map<SymbolType, OptionPricing> optionPricingMap = Maps.newHashMap();

    public OptionManager(@Value("${market.risk-free-interest-rate}") double r) {
        optionPricingMap.put(SymbolType.CALL, new CallOptionPricing(r));
        optionPricingMap.put(SymbolType.PUT, new PutOptionPricing(r));
    }

    public void register(Option option) {
        optionPool.register(option);
    }

    public Option findOption(String optionSymbol) {
        return optionPool.getOption(optionSymbol);
    }

    public OptionPricing getPricing(SymbolType type) {
        return optionPricingMap.get(type);
    }

    public void clear() {
        optionPool.clear();
    }
}
