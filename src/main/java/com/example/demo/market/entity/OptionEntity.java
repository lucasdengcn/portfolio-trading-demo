/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.market.entity;

import com.example.demo.model.SymbolType;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "options")
public class OptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String symbol;

    private double latestPrice;

    @Enumerated
    private SymbolType symbolType;

    private String relStockSymbol;

    private int maturity;

    private double strikePrice;
}
