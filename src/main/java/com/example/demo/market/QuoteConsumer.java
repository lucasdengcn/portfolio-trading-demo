package com.example.demo.market;

import com.example.demo.market.model.Quote;
import com.google.protobuf.ByteString;

public interface QuoteConsumer {

    void onEvent(ByteString quote);

}
