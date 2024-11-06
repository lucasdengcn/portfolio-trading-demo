package com.example.demo.market;

import com.example.demo.market.model.Quote;

public interface QuoteConsumer {

    void onEvent(Quote quote);

}
