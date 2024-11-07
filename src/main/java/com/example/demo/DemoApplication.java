/* (C) 2024 */ 

package com.example.demo;

import com.example.demo.market.producer.StockPool;
import com.example.demo.portfolio.consumer.OptionPool;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.entity.ProductType;
import com.example.demo.portfolio.service.PositionService;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private StockPool stockPool;

    @Autowired
    private PositionService positionService;

    @Autowired
    private CSVDataLoader csvDataLoader;

    @Autowired
    @Qualifier("callOptionPool") OptionPool callOptionPool;

    @Autowired
    @Qualifier("putOptionPool") OptionPool putOptionPool;

    @Override
    public void run(String... args) throws Exception {
        //
        loadPositionData();
    }

    public void loadPositionData() throws IOException {
        csvDataLoader.load(new Consumer<List<PositionEntity>>() {
            @Override
            public void accept(List<PositionEntity> positionEntities) {
                //
                positionService.save(positionEntities);
                //
                positionEntities.forEach(item -> {
                    if (item.getType().equals(ProductType.STOCK)) {
                        stockPool.registerSymbol(item.getSymbol());
                    } else if (item.getType().equals(ProductType.CALL)) {
                        callOptionPool.registerOption(item.getSymbol());
                    } else if (item.getType().equals(ProductType.PUT)) {
                        putOptionPool.registerOption(item.getSymbol());
                    }
                });
            }
        });
    }
}
