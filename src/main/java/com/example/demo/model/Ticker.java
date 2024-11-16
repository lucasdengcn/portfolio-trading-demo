/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticker {
    private String symbol;
    private String name;
    private String exchange;
    private double price;
    private long volume;
}
