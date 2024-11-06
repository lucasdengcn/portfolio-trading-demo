package com.example.demo.portfolio.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("POSITIONS")
public class PositionEntity {

    @Id
    private Integer id;

    private String symbol;

    @Column("POSITION_SIZE")
    private Integer positionSize;

    private ProductType type;

    public PositionEntity() {
    }

    public PositionEntity(String symbol, Integer positionSize, ProductType type) {
        this.symbol = symbol;
        this.positionSize = positionSize;
        this.type = type;
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

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }
}
