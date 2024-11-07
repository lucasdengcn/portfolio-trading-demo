/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("POSITIONS")
public class PositionEntity {

    @Id
    private Integer id;

    private String symbol;

    @Column("POSITION_SIZE")
    private Integer positionSize;

    private int symbolType;

    @Column("REL_STOCK_SYMBOL")
    private String relStockSymbol;

    public PositionEntity() {}

    public PositionEntity(String symbol, Integer positionSize) {
        this.symbol = symbol;
        this.positionSize = positionSize;
    }

    public PositionEntity(String symbol, Integer positionSize, int type) {
        this.symbol = symbol;
        this.positionSize = positionSize;
        this.symbolType = type;
    }

    public PositionEntity(String symbol, Integer positionSize, int type, String relStockSymbol) {
        this.symbol = symbol;
        this.positionSize = positionSize;
        this.symbolType = type;
        this.relStockSymbol = relStockSymbol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getPositionSize() {
        return positionSize;
    }

    public void setPositionSize(Integer positionSize) {
        this.positionSize = positionSize;
    }

    public int getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(int symbolType) {
        this.symbolType = symbolType;
    }

    public String getRelStockSymbol() {
        return relStockSymbol;
    }

    public void setRelStockSymbol(String relStockSymbol) {
        this.relStockSymbol = relStockSymbol;
    }
}
