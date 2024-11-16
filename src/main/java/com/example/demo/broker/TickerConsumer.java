/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.broker;

import com.google.protobuf.ByteString;
import org.springframework.lang.NonNull;

public interface TickerConsumer {

    void onEvent(@NonNull ByteString byteString);
}
