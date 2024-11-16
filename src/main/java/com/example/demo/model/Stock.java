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
public class Stock {
    private String symbol;
    private String name;
    private double price;
    private long volume;
    private double expectedReturn;
    private double deviation;
    private Date lastUpdated;
}
