/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.broker;

import com.example.demo.model.Ticker;
import org.springframework.lang.NonNull;

public interface TickerConsumer {
    /**
     *
     * @param ticker
     */
    void onEvent(@NonNull Ticker ticker);
}
