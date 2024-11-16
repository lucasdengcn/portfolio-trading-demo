/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio {
    private long id;
    private double totalNav;
    private List<Position> holdings;
    private LocalDateTime updateTime;
}
