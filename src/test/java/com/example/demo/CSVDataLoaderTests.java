package com.example.demo;

import com.example.demo.market.QuoteBroker;
import com.example.demo.market.StockPool;
import com.example.demo.portfolio.consumer.OptionPool;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.entity.ProductType;
import com.example.demo.portfolio.repository.PositionRepository;
import com.example.demo.portfolio.service.PositionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SpringBootTest
@ActiveProfiles("test")
class CSVDataLoaderTests {

    @Autowired
    private CSVDataLoader csvDataLoader;

    @Autowired @Qualifier("callOptionPool")
    OptionPool callOptionPool;

    @Autowired @Qualifier("putOptionPool")
    OptionPool putOptionPool;

    @Autowired
    private StockPool stockPool;

    @BeforeEach
    void setup (){

    }

    @Test
    void check_csv_file() throws IOException {
        Assertions.assertNotNull(csvDataLoader.getCsvFilePath());
        Path file = csvDataLoader.getCsvFile();
        boolean exists = file.toFile().exists();
        Assertions.assertTrue(exists);
    }

    @Test
    void load_csv_file() throws IOException {
        csvDataLoader.load(new Consumer<List<PositionEntity>>() {
            @Override
            public void accept(List<PositionEntity> positionEntities) {
                //
                Assertions.assertEquals(6, positionEntities.size());
                //
                long count = positionEntities.stream().filter(positionEntity -> positionEntity.getType().equals(ProductType.STOCK)).count();
                Assertions.assertEquals(2, count);
                //
                count = positionEntities.stream().filter(positionEntity -> positionEntity.getType().equals(ProductType.PUT)).count();
                Assertions.assertEquals(2, count);
                //
                count = positionEntities.stream().filter(positionEntity -> positionEntity.getType().equals(ProductType.CALL)).count();
                Assertions.assertEquals(2, count);
            }
        });
    }

    // @Test
    void load_csv_file_save() throws IOException {
        Assertions.assertEquals(2, stockPool.countOfStock());
        //
        Set<String> stocks = stockPool.getStocks();
        Assertions.assertTrue(stocks.contains("AAPL"));
        Assertions.assertTrue(stocks.contains("TELSA"));
        //
        Assertions.assertEquals(1, callOptionPool.getOptions("AAPL").size());
        Assertions.assertEquals(1, putOptionPool.getOptions("AAPL").size());
        //
        Assertions.assertEquals(1, callOptionPool.getOptions("TELSA").size());
        Assertions.assertEquals(1, putOptionPool.getOptions("TELSA").size());
    }
}