/* (C) 2024 */ 

package com.example.demo.market.producer;

import com.google.protobuf.ByteString;

public interface QuoteConsumer {

    void onEvent(ByteString byteString);
}
