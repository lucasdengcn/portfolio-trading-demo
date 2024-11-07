/* lucas, yamingdeng@outlook.com (C) 2024 */ 

package com.example.demo.portfolio.service;

import com.example.demo.market.model.Option;
import com.example.demo.market.model.Quote;
import com.example.demo.market.model.Stock;
import com.example.demo.market.option.OptionManager;
import com.example.demo.market.stock.StockPool;
import com.example.demo.portfolio.entity.PositionEntity;
import com.example.demo.portfolio.model.Position;
import com.example.demo.portfolio.model.SymbolType;
import com.google.common.collect.Lists;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
        Stock stock = Stock.newBuilder()
                .setSymbol("A")
                .setExpectedReturn(110.0f)
                .setDeviation(0.12)
                .build();
        stockPool.registerPrice(stock);
        //
        Option option = Option.newBuilder()
                .setSymbol("A-100-C")
                .setMaturity(1)
                .setSymbolType(SymbolType.CALL_VALUE)
                .setStrikePrice(80.0f)
                .build();
        optionManager.register(option);
        option = Option.newBuilder()
                .setSymbol("A-100-P")
                .setMaturity(1)
                .setSymbolType(SymbolType.PUT_VALUE)
                .setStrikePrice(105.0f)
                .build();
        optionManager.register(option);
        //
        PositionEntity positionEntity = new PositionEntity("A", 1, SymbolType.STOCK_VALUE, "");
        PositionEntity positionEntity2 = new PositionEntity("A-100-P", -1, SymbolType.PUT_VALUE, "A");
        PositionEntity positionEntity3 = new PositionEntity("A-100-C", 1, SymbolType.CALL_VALUE, "A");
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
}
