/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    private long positionId;
    private String symbol;
    private double price;
    private long qty;
    private double nav;
    private SymbolType symbolType;
    private LocalDateTime updateTime;
    private String stockSymbol;
}
