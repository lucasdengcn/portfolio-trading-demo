package com.example.demo.portfolio.consumer;

import com.example.demo.market.model.Quote;

import java.util.Set;

public class PutOptionConsumerImpl extends AbstractOptionConsumerImpl {

    public PutOptionConsumerImpl(OptionPool putOptionPool) {
        super(putOptionPool);
    }

    @Override
    public void calculate(Quote stockQuote){
        Set<String> stringSet = optionPool.getOptions(stockQuote.getSymbol());
        if (null == stringSet){
            return;
        }
        stringSet.stream().map(s -> {
            Quote quote = quotePool.poll();
            assert quote != null;
            quote.toBuilder().setSymbol(s).build();
            return quote;
        });
    }

}
