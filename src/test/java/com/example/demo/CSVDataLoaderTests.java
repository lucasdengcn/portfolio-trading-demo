/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo;

import com.example.demo.loader.CSVDataLoader;
import com.example.demo.market.option.OptionManager;
import com.example.demo.market.stock.StockPool;
import com.example.demo.portfolio.service.PositionService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
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

    @Autowired
    private PositionService positionService;

    @BeforeEach
    void setup() {}

    @Test
    void check_csv_file() throws IOException {
        Path file = csvDataLoader.getFullPath("csv/sample-portfolio.csv");
        boolean exists = file.toFile().exists();
        Assertions.assertTrue(exists);
    }

    @Test
    void load_csv_files() throws IOException {
        Assertions.assertEquals(2, stockPool.countOfStock());
        //
        List<String> stocks = stockPool.getStocks();
        Assertions.assertTrue(stocks.contains("AAPL"));
        Assertions.assertTrue(stocks.contains("TELSA"));
        //
        int count = optionManager.getOptionPool().count();
        Assertions.assertEquals(4, count);
        //
        count = positionService.count();
        Assertions.assertEquals(6, count);
    }
}
