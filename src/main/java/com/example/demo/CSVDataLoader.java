package com.example.demo;

import com.example.demo.market.ListedStock;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.entity.ProductType;
import com.example.demo.portfolio.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CSVDataLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${app.csv-relative-path}")
    private String csvFilePath;

    @Autowired
    private final ListedStock listedStock;

    @Autowired
    private final PositionService positionService;

    public CSVDataLoader(ListedStock listedStock, PositionService positionService) {
        this.listedStock = listedStock;
        this.positionService = positionService;
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public Path getCsvFile() throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        if (csvFilePath.startsWith("/")){
            path = path + csvFilePath;
        } else {
            path = path + "/" + csvFilePath;
        }
        return Paths.get(path);
    }

    public void load() throws IOException {
        Path csvFile = getCsvFile();
        logger.debug("csv file: {}", csvFile.toAbsolutePath());
        List<PositionEntity> entityList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(csvFile)) {
            stream.forEach(s -> {
                if (s.startsWith("symbol")){
                    return;
                }
                String[] temp = s.split(",");
                ProductType productType = null;
                if (temp[0].endsWith("-C")){
                    productType = ProductType.CALL;
                } else if (temp[0].endsWith("-P")){
                    productType = ProductType.PUT;
                } else {
                    productType = ProductType.STOCK;
                }
                //
                entityList.add(new PositionEntity(temp[0], Integer.parseInt(temp[1]), productType));
                if (productType == ProductType.STOCK){
                    listedStock.registerSymbol(temp[0]);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("load stocks in total: {}, positions in total: {}", listedStock.count(), entityList.size());
        //
        positionService.save(entityList);
    }

}
