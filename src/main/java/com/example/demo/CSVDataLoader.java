package com.example.demo;

import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.entity.ProductType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Component
public class CSVDataLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${app.csv-relative-path}")
    private String csvFilePath;

    public CSVDataLoader() {
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

    public void load(Consumer<List<PositionEntity>> entityConsumer) throws IOException {
        Path csvFile = getCsvFile();
        logger.debug("csv file: {}", csvFile.toAbsolutePath());
        List<PositionEntity> entityList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(csvFile)) {
            stream.forEach(s -> {
                if (s.startsWith("symbol")){
                    // the head row
                    return;
                }
                // for demo
                String[] temp = s.split(",");
                PositionEntity entity = new PositionEntity(temp[0], Integer.parseInt(temp[1]));
                //
                if (temp[0].endsWith("-C")){
                    entity.setRelStockSymbol(temp[0].split("-")[0]);
                    entity.setType(ProductType.CALL);
                } else if (temp[0].endsWith("-P")){
                    entity.setRelStockSymbol(temp[0].split("-")[0]);
                    entity.setType(ProductType.PUT);
                } else {
                    entity.setType(ProductType.STOCK);
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

}
