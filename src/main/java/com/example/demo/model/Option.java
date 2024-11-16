/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Option {
    private String symbol;
    private String name;
    private double price;
    private double strikePrice;
    private int maturity;
    private long volume;
    private SymbolType symbolType;
    private String stockSymbol;
    private Date lastUpdated;
}
