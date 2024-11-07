/* (C) 2024 */ 

package com.example.demo;

import com.example.demo.market.producer.StockPool;
import com.example.demo.portfolio.model.SymbolType;
import com.example.demo.portfolio.service.OptionManager;
import com.example.demo.portfolio.entity.PositionEntity;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CSVDataLoaderTests {

    @Autowired
    private CSVDataLoader csvDataLoader;

    @Autowired
    private StockPool stockPool;

    @Autowired
    private OptionManager optionManager;

    @BeforeEach
    void setup() {}

    @Test
    void check_csv_file() throws IOException {
        Path file = csvDataLoader.getFullPath("csv/sample-portfolio.csv");
        boolean exists = file.toFile().exists();
        Assertions.assertTrue(exists);
    }

    @Test
    void load_Position_csv_file() throws IOException {
        csvDataLoader.loadPosition(new Consumer<List<PositionEntity>>() {
            @Override
            public void accept(List<PositionEntity> positionEntities) {
                //
                Assertions.assertEquals(6, positionEntities.size());
                //
                long count = positionEntities.stream()
                        .filter(positionEntity -> positionEntity.getSymbolType() == SymbolType.STOCK_VALUE)
                        .count();
                Assertions.assertEquals(2, count);
                //
                count = positionEntities.stream()
                        .filter(positionEntity -> positionEntity.getSymbolType() == SymbolType.PUT_VALUE)
                        .count();
                Assertions.assertEquals(2, count);
                //
                count = positionEntities.stream()
                        .filter(positionEntity -> positionEntity.getSymbolType() == SymbolType.CALL_VALUE)
                        .count();
                Assertions.assertEquals(2, count);
            }
        });
    }

    // @Test
    void load_Position_csv_file_save() throws IOException {
        Assertions.assertEquals(2, stockPool.countOfStock());
        //
        List<String> stocks = stockPool.getStocks();
        Assertions.assertTrue(stocks.contains("AAPL"));
        Assertions.assertTrue(stocks.contains("TELSA"));
        //
        Assertions.assertEquals(1, optionManager.findSymbols(SymbolType.CALL, "AAPL").size());
        Assertions.assertEquals(1, optionManager.findSymbols(SymbolType.PUT, "AAPL").size());
        //
        Assertions.assertEquals(1, optionManager.findSymbols(SymbolType.CALL, "TELSA").size());
        Assertions.assertEquals(1, optionManager.findSymbols(SymbolType.PUT, "TELSA").size());
    }
}
