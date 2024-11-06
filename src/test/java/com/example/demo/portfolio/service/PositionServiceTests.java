package com.example.demo.portfolio.service;

import com.example.demo.market.model.Quote;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.entity.ProductType;
import com.example.demo.portfolio.model.Position;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PositionServiceTests {

    @Autowired
    private PositionService positionService;

    @BeforeEach
    void setup(){
        positionService.deleteAll();
    }

    @Test
    void save_positions() {
        PositionEntity positionEntity = new PositionEntity("A", 1, ProductType.STOCK);
        PositionEntity positionEntity2 = new PositionEntity("A-100-P", -1, ProductType.PUT);
        PositionEntity positionEntity3 = new PositionEntity("A-100-C", 1, ProductType.CALL);
        positionService.save(Lists.newArrayList(positionEntity, positionEntity2, positionEntity3));
        //
        Assertions.assertEquals(3, positionService.count());
    }

    @Test
    void update_nav_on_price_change_on_stock() {
        save_positions();
        //
        Quote quote = Quote.newBuilder().setPrice(100.0f).setSymbol("A").build();
        positionService.updateOnPriceChange(quote);
        //
        Position position = positionService.findBySymbol("A");
        Assertions.assertEquals(1, position.getQty());
        Assertions.assertEquals(100.0f, position.getNav());
        Assertions.assertEquals(100.0f, position.getPrice());
    }

    @Test
    void update_nav_on_price_change_on_put() {
        save_positions();
        //
        Quote quote = Quote.newBuilder().setPrice(100.0f).setSymbol("A-100-P").build();
        positionService.updateOnPriceChange(quote);
        //
        Position position = positionService.findBySymbol("A-100-P");
        Assertions.assertEquals(-1, position.getQty());
        Assertions.assertEquals(-100.0f, position.getNav());
        Assertions.assertEquals(100.0f, position.getPrice());
    }

    @Test
    void update_nav_on_price_change_on_call() {
        save_positions();
        //
        Quote quote = Quote.newBuilder().setPrice(100.0f).setSymbol("A-100-C").build();
        positionService.updateOnPriceChange(quote);
        //
        Position position = positionService.findBySymbol("A-100-C");
        Assertions.assertEquals(1, position.getQty());
        Assertions.assertEquals(100.0f, position.getNav());
        Assertions.assertEquals(100.0f, position.getPrice());
    }
}