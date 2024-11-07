/* (C) 2024 */ 

package com.example.demo.market.producer;

import com.google.protobuf.ByteString;
import org.springframework.lang.NonNull;

public interface QuoteConsumer {

    void onEvent(@NonNull ByteString byteString);
}
