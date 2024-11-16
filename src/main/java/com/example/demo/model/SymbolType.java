/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.model;

public enum SymbolType {
    UNKNOWN(0),
    STOCK(1),
    CALL(2),
    PUT(3);

    private final int value;

    SymbolType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SymbolType of(int value) {
        if (value == STOCK.value) {
            return STOCK;
        } else if (value == CALL.value) {
            return CALL;
        } else if (value == PUT.value) {
            return PUT;
        } else {
            return UNKNOWN;
        }
    }
}
