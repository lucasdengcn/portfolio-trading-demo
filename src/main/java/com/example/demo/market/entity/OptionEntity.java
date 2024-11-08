/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("OPTIONS")
public class OptionEntity {

    @Id
    private Integer id;

    private String symbol;

    @Column("LATEST_PRICE")
    private double latestPrice;

    private int symbolType;

    @Column("REL_STOCK_SYMBOL")
    private String relStockSymbol;

    private int maturity;

    @Column("STRIKE_PRICE")
    private double strikePrice;

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

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
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

    public int getMaturity() {
        return maturity;
    }

    public void setMaturity(int maturity) {
        this.maturity = maturity;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }
}
