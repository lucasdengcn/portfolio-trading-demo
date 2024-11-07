/* (C) 2024 */ 

package com.example.demo;

import com.example.demo.market.model.Stock;
import com.example.demo.market.producer.StockPool;
import com.example.demo.portfolio.entity.PositionEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.example.demo.portfolio.model.SymbolType;
import com.example.demo.portfolio.service.OptionManager;
import com.example.demo.portfolio.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CSVDataLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${app.portfolio-relative-path}")
    private String portfolioFilePath;

    @Value("${app.stock-relative-path}")
    private String stockFilePath;

    @Autowired
    private StockPool stockPool;

    @Autowired
    private PositionService positionService;

    @Autowired
    private OptionManager optionManager;

    public CSVDataLoader() {}

    public String getCsvFilePath() {
        return portfolioFilePath;
    }

    public Path getFullPath(String relativePath) throws IOException {
        String path = Paths.get("").toAbsolutePath().toString();
        if (relativePath.startsWith("/")) {
            path = path + relativePath;
        } else {
            path = path + "/" + relativePath;
        }
        return Paths.get(path);
    }

    public void loadPosition(Consumer<List<PositionEntity>> entityConsumer) throws IOException {
        Path csvFile = getFullPath(portfolioFilePath);
        logger.debug("portfolio csv file: {}", csvFile.toAbsolutePath());
        List<PositionEntity> entityList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(csvFile)) {
            stream.forEach(s -> {
                if (s.startsWith("symbol")) {
                    // the head row
                    return;
                }
                // for demo
                String[] temp = s.split(",");
                PositionEntity entity = new PositionEntity(temp[0], Integer.parseInt(temp[1]));
                //
                if (temp[0].endsWith("-C")) {
                    entity.setRelStockSymbol(temp[0].split("-")[0]);
                    entity.setSymbolType(SymbolType.CALL.getNumber());
                } else if (temp[0].endsWith("-P")) {
                    entity.setRelStockSymbol(temp[0].split("-")[0]);
                    entity.setSymbolType(SymbolType.PUT.getNumber());
                } else {
                    entity.setSymbolType(SymbolType.STOCK.getNumber());
                }
                //
                entityList.add(entity);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("load positions in total: {}", entityList.size());
        //
        entityConsumer.accept(entityList);
    }

    public void loadPositionAndSave() throws IOException {
        this.loadPosition(new Consumer<List<PositionEntity>>() {
            @Override
            public void accept(List<PositionEntity> positionEntities) {
                //
                positionService.save(positionEntities);
                //
                positionEntities.forEach(item -> {
                    if (item.getSymbolType() == SymbolType.STOCK_VALUE) {
                        stockPool.register(item.getSymbol());
                    } else if (item.getSymbolType() == SymbolType.CALL_VALUE) {
                        optionManager.register(SymbolType.CALL, item.getSymbol());
                    } else if (item.getSymbolType() == SymbolType.PUT_VALUE) {
                        optionManager.register(SymbolType.PUT, item.getSymbol());
                    }
                });
            }
        });
    }

    public void loadStockPrices() throws IOException {
        Path csvFile = getFullPath(stockFilePath);
        logger.debug("stock csv file: {}", csvFile.toAbsolutePath());
        AtomicInteger count = new AtomicInteger();
        try (Stream<String> stream = Files.lines(csvFile)) {
            stream.forEach(s -> {
                if (s.startsWith("symbol")) {
                    // the head row
                    return;
                }
                // for demo
                // symbol,price,expected_return,deviation
                String[] temp = s.split(",");
                Stock stock = Stock.newBuilder().setSymbol(temp[0])
                        .setPrice(new BigDecimal(temp[1]).doubleValue())
                        .setExpectedReturn(new BigDecimal(temp[2]).doubleValue())
                        .setDeviation(new BigDecimal(temp[3]).doubleValue())
                        .build();
                stockPool.registerPrice(stock);
                count.getAndIncrement();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("load stocks in total: {}", count.get());
    }

}
