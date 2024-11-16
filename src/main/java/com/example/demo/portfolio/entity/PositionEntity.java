/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.entity;

import com.example.demo.model.SymbolType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Position Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "POSITIONS")
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String symbol;

    private Integer positionSize;

    @Enumerated
    private SymbolType symbolType;

    private String relStockSymbol;

    public PositionEntity(String symbol, Integer positionSize) {
        this.symbol = symbol;
        this.positionSize = positionSize;
    }

    public PositionEntity(String symbol, Integer positionSize, SymbolType type) {
        this.symbol = symbol;
        this.positionSize = positionSize;
        this.symbolType = type;
    }

    public PositionEntity(String symbol, Integer positionSize, SymbolType type, String relStockSymbol) {
        this.symbol = symbol;
        this.positionSize = positionSize;
        this.symbolType = type;
        this.relStockSymbol = relStockSymbol;
    }
}
