/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.loader;

import com.example.demo.market.option.OptionManager;
import com.example.demo.market.stock.StockPool;
import com.example.demo.model.Option;
import com.example.demo.model.Stock;
import com.example.demo.model.SymbolType;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.service.PositionService;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
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

    @Value("${app.option-relative-path}")
    private String optionFilePath;

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

    public void loadAll() throws IOException {
        loadStocks();
        loadOptions();
        loadPosition();
    }

    public void loadPosition() throws IOException {
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
                    entity.setRelStockSymbol(temp[2]);
                    entity.setSymbolType(SymbolType.CALL);
                } else if (temp[0].endsWith("-P")) {
                    entity.setRelStockSymbol(temp[2]);
                    entity.setSymbolType(SymbolType.PUT);
                } else {
                    entity.setRelStockSymbol("");
                    entity.setSymbolType(SymbolType.STOCK);
                }
                //
                entityList.add(entity);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("load positions in total: {}", entityList.size());
        //
        positionService.save(entityList);
    }

    public void loadStocks() throws IOException {
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
                Stock stock = Stock.builder()
                        .symbol(temp[0])
                        .price(new BigDecimal(temp[1]).doubleValue())
                        .expectedReturn(new BigDecimal(temp[2]).doubleValue())
                        .deviation(new BigDecimal(temp[3]).doubleValue())
                        .build();
                stockPool.registerPrice(stock);
                count.getAndIncrement();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("load stocks in total: {}", count.get());
    }

    public void loadOptions() throws IOException {
        Path csvFile = getFullPath(optionFilePath);
        logger.debug("option csv file: {}", csvFile.toAbsolutePath());
        AtomicInteger count = new AtomicInteger();
        try (Stream<String> stream = Files.lines(csvFile)) {
            stream.forEach(s -> {
                if (s.startsWith("symbol")) {
                    // the head row
                    return;
                }
                // for demo
                // symbol,strikePrice,maturity(years),symbolType,stockSymbol
                String[] temp = s.split(",");
                Option option = Option.builder()
                        .symbol(temp[0])
                        .strikePrice(new BigDecimal(temp[1]).doubleValue())
                        .maturity(Integer.parseInt(temp[2]))
                        .symbolType(SymbolType.valueOf(temp[3]))
                        .stockSymbol(temp[4])
                        .build();
                //
                optionManager.register(option);
                count.getAndIncrement();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("load options in total: {}", count.get());
    }
}
