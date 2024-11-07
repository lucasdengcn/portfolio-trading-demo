/* (C) 2024 */ 

package com.example.demo.portfolio.service;

import com.example.demo.market.model.Quote;
import com.example.demo.market.producer.StockPool;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.model.Position;
import com.example.demo.portfolio.model.SymbolType;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class PositionServiceTests {

    @Autowired
    private PositionService positionService;

    @Autowired
    private OptionManager optionManager;

    @Autowired
    private StockPool stockPool;

    @BeforeEach
    void setup() {
        positionService.deleteAll();
        optionManager.clear();
    }

    @Test
    void save_positions() {
        stockPool.register("A");
        optionManager.register(SymbolType.CALL, "A-100-C");
        optionManager.register(SymbolType.PUT, "A-100-P");
        //
        PositionEntity positionEntity = new PositionEntity("A", 1, SymbolType.STOCK_VALUE);
        PositionEntity positionEntity2 = new PositionEntity("A-100-P", -1, SymbolType.PUT_VALUE);
        PositionEntity positionEntity3 = new PositionEntity("A-100-C", 1, SymbolType.CALL_VALUE);
        positionService.save(Lists.newArrayList(positionEntity, positionEntity2, positionEntity3));
        //
        Assertions.assertEquals(3, positionService.count());
    }

    @Test
    void update_nav_on_stock_price_change() {
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
    void test_sum_of_nav() {
        Position position = Position.newBuilder().setNav(1.0f).build();
        List<Position> positionList = Lists.newArrayList(position, position, position);
        double sumOfNav = positionService.getSumOfNav(positionList);
        Assertions.assertEquals(3.0f, sumOfNav);
    }

    @Test
    void test_update_option_nav() {
        Quote quote = Quote.newBuilder().setPrice(100.0f).setSymbol("A").build();
        Position position = Position.newBuilder().setSymbolType(SymbolType.CALL).setQty(1).setSymbol("A-100-C").build();
        //
        Position positionUpdated = positionService.updateOptionNav(quote, position);
        Assertions.assertEquals(100.0f, positionUpdated.getNav());
        Assertions.assertEquals(100.0f, positionUpdated.getPrice());
    }

}
