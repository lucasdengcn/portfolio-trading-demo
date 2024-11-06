package com.example.demo;

import com.example.demo.market.ListedStock;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.entity.ProductType;
import com.example.demo.portfolio.repository.PositionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CSVDataLoaderTests {

    @Autowired
    private CSVDataLoader csvDataLoader;

    @Autowired
    private ListedStock listedStock;

    @Autowired
    private PositionRepository positionRepository;

    @Test
    void check_csv_file() throws IOException {
        Assertions.assertNotNull(csvDataLoader.getCsvFilePath());
        Path file = csvDataLoader.getCsvFile();
        boolean exists = file.toFile().exists();
        Assertions.assertTrue(exists);
    }

    @Test
    void load_csv_file() throws IOException {
        csvDataLoader.load();
        Set<String> symbols = listedStock.getSymbols();
        Assertions.assertEquals(2, symbols.size());
        Assertions.assertTrue(symbols.contains("TELSA"));
        Assertions.assertTrue(symbols.contains("AAPL"));
        //
        int count = positionRepository.countByType(ProductType.STOCK);
        Assertions.assertEquals(2, count);
    }

}